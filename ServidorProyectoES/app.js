'use strict';

var express = require('express');
var hash = require('./hash.js');
var bodyParser = require('body-parser');
var app = express();
var upload = require("express-fileupload");
var dateFormat = require('dateformat');
var fs = require("fs");
app.use(upload());

const config = require('./config.js');

var knex= null;

// asume que el cuerpo del mensaje de la petición está en JSON:
app.use(bodyParser.json());

// inicializa Knex.js para usar diferentes bases de datos según el entorno:
function conectaBD () {
  if (knex===null) {
    var options;
    options= config.localbd;
    console.log('Usando SQLite como base de datos local');
    // Muestra la conversión a SQL de cada consulta:
    // options.debug= true;
    knex= require('knex')(options);
  }
}

// crea las tablas si no existen:
async function creaEsquema(res) {
  try {
    let existeTabla= await knex.schema.hasTable('usuarios');
    if (!existeTabla) {
      await knex.schema.createTable('usuarios', (tabla) => {
        tabla.increments('usuario_id').primary();
        tabla.string('usuario', 50).unique();
        tabla.string('password', 128);
        tabla.string('salt',32);
      });
      console.log("Se ha creado la tabla usuarios");
    }
    existeTabla= await knex.schema.hasTable('ficheros');
    if (!existeTabla) {
      await knex.schema.createTable('ficheros', (tabla) => {
        tabla.increments('fichero_id').primary();
        tabla.string('nombre');
        tabla.string('ruta').unique();
        tabla.string('propietario', 50);
        tabla.datetime('fecha_modificacion');
        tabla.boolean('copia_diaria');
        tabla.boolean('copia_semanal');
        tabla.boolean('copia_mensual');
        tabla.string('clave');
      });
      console.log("Se ha creado la tabla ficheros");
    }
    existeTabla= await knex.schema.hasTable('comparticion_ficheros');
    if (!existeTabla) {
      await knex.schema.createTable('comparticion_ficheros', (tabla) => {
        tabla.increments('ficheroCompartido_id').primary();
        tabla.integer('fichero_id');
        tabla.integer('propietario');
        tabla.integer('compartido');
        tabla.string('rutaFichero');
        tabla.string('nombreFichero');
        tabla.string('claveCompartida');
        tabla.unique(['propietario', 'compartido', 'rutaFichero']);
      });
      console.log("Se ha creado la tabla comparticion_ficheros");
    }
  }
  catch (error) {
    console.log(`Error al crear las tablas: ${error}`);
    res.status(404).send({ result:null,error:'error al crear la tabla; contacta con el administrador' });
  }
}

//CARGAR PUBLIC
const path = require('path');
const publico = path.join(__dirname, 'public');
app.use('/', express.static(publico));

// middleware para aceptar caracteres UTF-8 en la URL:
app.use( (req, res, next) => {
  req.url = decodeURI(req.url);
  next();
});

// middleware para las cabeceras de CORS:
app.use( (req, res, next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header('Access-Control-Allow-Methods', 'DELETE, PUT, GET, POST, OPTIONS');
  res.header("Access-Control-Allow-Headers", "content-type");
  next();
});

app.use( async (req, res, next) => {
  app.locals.knex= conectaBD(app.locals.knex);
  await creaEsquema(res);
  next();
});

app.get('/', function (req, res) {
  res.send('Hello World!');
});

app.post('/login', async (req,res) => {
  try {
    const usuario = req.body.usuario;
    const password = req.body.password;
    const userData = await knex('usuarios').select('password','salt', 'usuario_id').where('usuario',usuario).first();
    if(userData == null){
      res.status(200).send({result:"ERROR", error: "El usuario no existe"});
      return;
    }
    const usuario_id = userData.usuario_id;
    if(hash.saltHashPasswordCOMPROBAR(password, userData.salt, userData.password)){
      res.status(200).send({result:"OK", userId: usuario_id, error: null});
      return;
    }else{
      res.status(200).send({result:"ERROR", error: "Usuario/Contraseña incorrectos"});
      return;
    }
  }catch (err) {
    console.log(err);
    res.status(404).send({result:"ERROR", error: err});
    return;
  }
});

app.post('/registrarse', async (req,res) => {
  try {
    const usuario = req.body.usuario;
    const password = req.body.password;
    const passwordData = hash.saltHashPassword(password);
    const publicKey = req.files.publicKey;
    const privateKey = req.files.privateKey;
    const keysRoute = "usersKeys/"+usuario + "/";
    var fila = {usuario: usuario, password: passwordData.passwordHash, salt: passwordData.salt};
    const user_id = await knex('usuarios').insert(fila);
    if (!fs.existsSync(keysRoute)) {
      fs.mkdirSync(keysRoute);
    }
    publicKey.mv(keysRoute + publicKey.name, function(err){
      if(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
      }
    });
    privateKey.mv(keysRoute + privateKey.name, function(err){
      if(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
      }
    });
    res.status(200).send({result:"OK", userId: user_id[0], error: null});
  }catch (err) {
    console.log(err);
    res.status(404).send({result:"ERROR", error: err.toString()});
    return;
  }
});

app.post('/subirFichero', async (req,res) => {
  if(req.files){
    const file = req.files.file1;
    const user = req.body.usuario;
    const copia_diaria = req.body.copia_diaria;
    const copia_semanal = req.body.copia_semanal;
    const copia_mensual = req.body.copia_mensual;
    const filename = file.name;
    const folderRoute =  "uploadedFiles/" + user;
    const fileRoute =  folderRoute + "/" + filename;
    const clave = req.body.clave;
    if (!fs.existsSync(folderRoute)) {
      fs.mkdirSync(folderRoute);
    }
    file.mv(fileRoute, function(err){
      if(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
      }
    });
    //INSERTAR EN BD EL ARCHIVO, PROPIETARIO Y RUTA DEL FICHERO, ADEMÁS DE FECHA DE GUARDADO
    try{
      var fila = {nombre: filename, ruta: fileRoute, propietario: user, fecha_modificacion: dateFormat(Date.now(), "dd-mm-yyyy HH:MM:ss"),
                  copia_diaria: copia_diaria, copia_semanal: copia_semanal, copia_mensual: copia_mensual, clave: clave};
      const ficheroId = await knex('ficheros').insert(fila);
      res.status(200).send({result:"OK", ficheroId: ficheroId[0], error: null});
    }catch(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
    }
  }
});

app.post('/añadirCompartido', async (req,res) => {
    const ficheroId = req.body.ficheroId; 
    const propietario = req.body.propietario;
    const compartido = req.body.compartido;
    const nombre = req.body.nombre;
    const clave = req.body.clave;
    const fileRoute =  "uploadedFiles/" + propietario + "/" + nombre;
    //INSERTAR EN BD EL ARCHIVO, PROPIETARIO Y RUTA DEL FICHERO, ADEMÁS DE FECHA DE GUARDADO
    try{
      var fila = {fichero_id: ficheroId, propietario: propietario, compartido: compartido, rutaFichero: fileRoute, nombreFichero: nombre,  claveCompartida: clave};
      await knex('comparticion_ficheros').insert(fila);
      res.status(200).send({result:"OK", error: null});
    }catch(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
    }
});

app.get('/obtenerListaFicheros', async (req,res) => {
    try{
      const userId = req.query.userId;
      const userData = await knex('usuarios').select('usuario').where('usuario_id',userId).first();
      if(userData){
        const fileList = await knex('ficheros').select('fichero_id', 'nombre').where('propietario',userData.usuario);
        res.status(200).send({result:fileList, error: null});
        return;
      }else{
        console.log("El usuario no existe");
        res.status(404).send({result:"ERROR", error: "El usuario no existe"});
        return;
      }
    }catch(err){
      console.log(err);
      res.status(404).send({result:"ERROR", error: err});
      return;
    }
});

app.get('/obtenerFichero', async (req,res) => {
  //PASAR COMO ARGUMENTO ID DEL FICHERO Y BUSCAR EN BD LAS RUTAS DEL MISMO ETC, LUEGO DEVOLVERLAS
  try{
    const ficheroId = req.query.ficheroId;
    const rutaFichero = await knex('ficheros').select('ruta','nombre','clave').where('fichero_id',ficheroId).first();
    if (fs.existsSync(rutaFichero.ruta)) {
      fs.readFile( rutaFichero.ruta, function (err, data) {
        if (err) {
          res.status(404).send({result:null, error: err});
          return; 
        }
        res.status(200).send({data:data, filename: rutaFichero.nombre, clave: rutaFichero.clave});
      });
    }else{
      res.status(404).send({result:null, error: err});
    }
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/obtenerFicheroCompartido', async (req,res) => {
  //PASAR COMO ARGUMENTO ID DEL FICHERO Y BUSCAR EN BD LAS RUTAS DEL MISMO ETC, LUEGO DEVOLVERLAS
  try{
    const ficheroCompartidoId = req.query.ficheroCompartidoId;
    const file = await knex('comparticion_ficheros').select('rutaFichero','nombreFichero','claveCompartida').where('ficheroCompartido_id',ficheroCompartidoId).first();
    if (fs.existsSync(file.rutaFichero)) {
      fs.readFile(file.rutaFichero, function (err, data) {
        if (err) {
          res.status(404).send({result:null, error: err});
          return; 
        }
        res.status(200).send({data:data, filename: file.nombreFichero, clave: file.claveCompartida});
      });
    }else{
      res.status(404).send({result:null, error: err});
    }
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/obtenerClavePublica', async (req,res) => {
  //PASAR COMO ARGUMENTO NOMBRE DEL USUARIO
  try{
    const usuario = req.query.usuario;
    const rutaClavePublica = "usersKeys/"+usuario + "/publicKey_" + usuario;
    if (fs.existsSync(rutaClavePublica)) {
      fs.readFile( rutaClavePublica, function (err, data) {
        if (err) {
          res.status(404).send({result:null, error: err});
          return; 
        }
        res.status(200).send({data:data, filename: "publicKey_" + usuario});
      });
    }else{
      res.status(404).send({result:null, error: err});
    }
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/obtenerClavePrivada', async (req,res) => {
  //PASAR COMO ARGUMENTO NOMBRE DEL USUARIO
  try{
    const usuario = req.query.usuario;
    const rutaClavePrivada = "usersKeys/"+usuario + "/privateKey_" + usuario;
    if (fs.existsSync(rutaClavePrivada)) {
      fs.readFile( rutaClavePrivada, function (err, data) {
        if (err) {
          res.status(404).send({result:null, error: err});
          return; 
        }
        res.status(200).send({data:data, filename: "privateKey_" + usuario});
      });
    }else{
      res.status(404).send({result:null, error: err});
    }
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/compartidos/otros', async (req,res) => {
  try{
    const usuarioId = req.query.userId;
    const listaFicherosCompartidos = await knex('comparticion_ficheros').select('ficheroCompartido_id','nombreFichero').where('compartido',usuarioId);
    res.status(200).send({result:listaFicherosCompartidos});
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/compartidos/propios', async (req,res) => {
  try{
    const usuarioId = req.query.userId;
    const listaFicherosCompartidos = await knex('comparticion_ficheros').select('ficheroCompartido_id','nombreFichero').where('propietarioId',usuarioId);
    res.status(200).send({result:listaFicherosCompartidos});
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.get('/usuarios/lista', async (req,res) => {
  try{
    const usuarioId = req.query.userId;
    const listaUsuarios = await knex('usuarios').select('usuario_id','usuario').whereNot('usuario_id', usuarioId);
    res.status(200).send({result:listaUsuarios});
  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});

app.delete('/borrarFichero', async (req,res) => {
  const user = req.body.usuario;
  const ficheroId = req.body.ficheroId;
  const ficheroNombre = req.body.ficheroNombre;
  const folderRoute =  "uploadedFiles/" + user;
  const fileRoute =  folderRoute + "/" + ficheroNombre;
  
  try{ 
    fs.unlinkSync(fileRoute)
    //checkeorama
    //BORRAR EN BD EL ARCHIVO, PROPIETARIO Y RUTA DEL FICHERO, ADEMÁS DE FECHA DE GUARDADO
    await knex('ficheros').where({nombre: ficheroNombre}).del();
    res.status(200).send({result:"OK", error: null});

  }catch(err){
    console.log(err);
    res.status(404).send({result:null, error: err});
    return; 
  }
});


const PORT = process.env.PORT || 5000;

app.listen(PORT, function () {
  //SI NO EXISTE LA CARPETA DE LOS FICHEROS LA CREAMOS
  if (!fs.existsSync("uploadedFiles")) {
    fs.mkdirSync("uploadedFiles");
  }
  if (!fs.existsSync("usersKeys")) {
    fs.mkdirSync("usersKeys");
  }
  console.log(`Aplicación lanzada en el puerto ${ PORT }!`);
});