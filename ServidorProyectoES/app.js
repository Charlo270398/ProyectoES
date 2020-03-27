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
      });
      console.log("Se ha creado la tabla ficheros");
    }
    existeTabla= await knex.schema.hasTable('comparticion_ficheros');
    if (!existeTabla) {
      await knex.schema.createTable('comparticion_ficheros', (tabla) => {
        tabla.increments('ficheroCompartido_id').primary();
        tabla.integer('propietarioId');
        tabla.integer('compartidoId');
        tabla.string('rutaFichero');
        tabla.string('nombreFichero');
        tabla.unique(['propietarioId', 'compartidoId', 'rutaFichero']);
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
    var fila = {usuario: usuario, password: passwordData.passwordHash, salt: passwordData.salt};
    const user_id = await knex('usuarios').insert(fila);
    res.status(200).send({result:"OK", userId: user_id[0], error: null});
  }catch (err) {
    console.log(err);
    res.status(404).send({result:null, error: err});
    return;
  }
});

app.post('/subirFichero', async (req,res) => {
  if(req.files){
    var file = req.files.file1;
    var user = req.body.usuario;
    var filename = file.name;
    const folderRoute =  "uploadedFiles/" + user;
    const fileRoute =  folderRoute + "/" + filename;
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
      var fila = {nombre: filename, ruta: fileRoute, propietario: user, fecha_modificacion: dateFormat(Date.now(), "dd-mm-yyyy HH:MM:ss")};
      await knex('ficheros').insert(fila);
      res.status(200).send({result:"OK", error: null});
    }catch(err){
        console.log(err);
        res.status(404).send({result:"ERROR", error: err});
        return;
    }
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
    const rutaFichero = await knex('ficheros').select('ruta','nombre').where('fichero_id',ficheroId).first();
    if (fs.existsSync(rutaFichero.ruta)) {
      fs.readFile( rutaFichero.ruta, function (err, data) {
        if (err) {
          res.status(404).send({result:null, error: err});
          return; 
        }
        res.status(200).send({data:data, filename: rutaFichero.nombre});
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
    const listaFicherosCompartidos = await knex('comparticion_ficheros').select('ficheroCompartido_id','nombreFichero').where('compartidoId',usuarioId);
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
    const listaUsuarios = await knex('usuarios').select('usuario_id','usuario');
    res.status(200).send({result:listaUsuarios});
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
  console.log(`Aplicación lanzada en el puerto ${ PORT }!`);
});