//DEFINICIONES
const base="/cuestionario/v1";
const cabeceras= {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
}
//var cid = 1;//CONTADOR CUESTIONARIOS 

//FUNCIONES AUXILIARES

function insertAsLastChild(padre,nuevoHijo){
    padre.append(nuevoHijo);
}

function insertAsFirstChild(padre,nuevoHijo){
    padre.insertBefore(nuevoHijo, padre.firstChild);
}

function insertBeforeChild(padre, hijo, nuevoHijo){
    padre.insertBefore(nuevoHijo, hijo);
}

function removeElement(nodo){
    nodo.parentNode.removeChild(nodo);
}

function queryAncestorSelector(node,selector) {
    var parent= node.parentNode;
    var all = document.querySelectorAll(selector);
    var found= false;
    while (parent !== document && !found) {
        for (var i = 0; i < all.length && !found; i++) {
        found= (all[i] === parent)?true:false;
        }
        parent= (!found)?parent.parentNode:parent;
    }
    return (found)?parent:null;
}

function consolaError(err){
    console.log("Error: " + err);
}
//FUNCIONES 

function addCruz(nodoBloque) {
    let div= document.createElement('div');
    div.textContent='\u2612';//Caracter unicode 2612 equivale a la cruz
    div.className = 'borra';
    div.addEventListener("click", borraPregunta, false);//Listener para borrar cuando se hace click
    insertAsFirstChild(nodoBloque, div);
}

function borraPregunta(nodo){
    var pregunta = queryAncestorSelector(nodo.target, 'div.bloque');
    var section = queryAncestorSelector(pregunta, 'section');
    const id_pregunta = pregunta.getAttribute("pid");

    borrarPregunta(id_pregunta);
    pregunta.parentNode.removeChild(pregunta);
    if(!section.querySelector('.bloque')){//Si no quedan bloques en la seccion la eliminamos 
        borrarCuestionario(section.id)//Borramos el cuestionario de la BD 
        let aList= document.querySelector("header").querySelector("nav").querySelector("ul").querySelectorAll("a");
        aList.forEach(aElement => {
            if(aElement.hash == ('#' + section.id)){
                removeElement(aElement);
            }
        });
        removeElement(section);
    }
}

function addBloquePregunta(nodo, enunciado, respuesta, pregunta_id){
    //CREACION DEL ELEMENTO
    var añadir = true; 
    let divBloque= document.createElement('div');
    divBloque.className = 'bloque';
    divPregunta= document.createElement('div');
    divPregunta.className = 'pregunta';
    divRespuesta= document.createElement('div');
    divRespuesta.className = 'respuesta';

    //ID BLOQUE
    if(pregunta_id){
        divBloque.setAttribute("pid", pregunta_id);
    }else{
        alert("Campo PREGUNTA_ID vacío");
        añadir = false;
    }

    //ENUNCIADO
    if(enunciado){
        divPregunta.textContent = enunciado;
    }else{
        alert("Campo ENUNCIADO vacío");
        añadir = false;
    }
    
    //RESPUESTA
    if(respuesta != undefined && respuesta != null){
        if(respuesta){
            //VERDADERO
            divRespuesta.setAttribute("data-valor", true);
        }else{
            //FALSO
            divRespuesta.setAttribute("data-valor", false);
        }
    }else{
        alert("Campo RESPUESTA vacío");
        añadir = false;
    }

    if(añadir){
        divBloque.append(divPregunta);
        divBloque.append(divRespuesta);
        addCruz(divBloque);
        insertAsLastChild(queryAncestorSelector(nodo, 'section'), divBloque);
        //RESETEO DEL FORMULARIO
        const id_cuestionario = queryAncestorSelector(nodo, 'section').id; //Obtenemos ID de la seccion
        queryAncestorSelector(nodo, 'ul').querySelector(`input[name="${id_cuestionario + "_pregunta"}"]`).value = "";
        queryAncestorSelector(nodo, 'ul').querySelector(`input[name="${id_cuestionario + "_respuesta"}"]`).checked = true;
    }
}

function addFormPregunta(nodoSection){
    //DIV
    let divPregunta= document.createElement('div');
    divPregunta.className = 'formulario';
    //UL
    let ulPregunta= document.createElement('ul');
    divPregunta.append(ulPregunta);
    //LI ENUNCIADO
    let liEnunciado;
    liEnunciado = document.createElement('li');
    ulPregunta.append(liEnunciado);
    
    labelEnunciado = document.createElement('label');
    labelEnunciado.textContent = "Enunciado de la pregunta:";
    liEnunciado.append(labelEnunciado);

    inputLiEnunciado = document.createElement('input');
    inputLiEnunciado.name = nodoSection.id + "_pregunta";
    inputLiEnunciado.type = "text";
    liEnunciado.append(inputLiEnunciado);
    //LI RESPUESTA
    let liRespuesta;
    liRespuesta = document.createElement('li');
    ulPregunta.append(liRespuesta);

    labelRespuesta1 = document.createElement('label');
    labelRespuesta1.textContent = "Respuesta:";
    liRespuesta.append(labelRespuesta1);

    inputLiRespuesta1 = document.createElement('input');
    inputLiRespuesta1.name = nodoSection.id + "_respuesta";
    inputLiRespuesta1.type = "radio";
    inputLiRespuesta1.value = "verdadero";
    inputLiRespuesta1.checked = "true";
    liRespuesta.append(inputLiRespuesta1);
    
    textVerdadero = document.createElement('text');
    textVerdadero.textContent = 'Verdadero';
    liRespuesta.append(textVerdadero);

    inputLiRespuesta2 = document.createElement('input');
    inputLiRespuesta2.name = nodoSection.id + "_respuesta";
    inputLiRespuesta2.type = "radio";
    inputLiRespuesta2.value = "falso";
    liRespuesta.append(inputLiRespuesta2);

    textFalso = document.createElement('text');
    textFalso.textContent = 'Falso';
    liRespuesta.append(textFalso);

    //LI BOTON
    let liBoton;
    liBoton = document.createElement('li');
    ulPregunta.append(liBoton);

    inputLiBoton = document.createElement('input');
    inputLiBoton.type = "button";
    inputLiBoton.value = "Añadir nueva pregunta";
    liBoton.append(inputLiBoton);

    inputLiBoton.addEventListener("click", addPregunta, false);
    insertBeforeChild(nodoSection, nodoSection.querySelector(".bloque"), divPregunta);//Delante del primer bloque
    return nodoSection; //Devolvemos el nodo del formulario creado
}

function addSection(nodo, tema, cuestionario_id){
    section = document.createElement('section');
    section.id = cuestionario_id;
    
    //Creamos el componente del encabezado y le añadimos el tema
    encabezado = document.createElement('encabezado-cuestionario');
    encabezado.setAttribute("tema", tema);

    section.append(encabezado);
    addFormPregunta(section);
    insertAsLastChild(queryAncestorSelector(nodo, 'main'), section);

    li = document.createElement('li');
    a = document.createElement('a');
    li.append(a);
    a.textContent = tema;
    a.setAttribute("href", "#" + cuestionario_id);
    //cid += 1; //Incrementamos id del siguiente section

    lastChild = document.querySelector("header").querySelector("nav").querySelector("ul");//Insertamos en la lista dentro del nav del header
    insertAsLastChild(lastChild, li);

    //Borrado del formulario
    queryAncestorSelector(nodo, 'ul').querySelector("input[name=tema]").value = "";
    return section;//Devolvemos la sección
}


//FUNCIONES PRACTICA 4

//añadir un tema de cuestionario (POST) y devolver el id asignado en la base de datos;
function addCuestionario(nodo){
    event.preventDefault();
    var tema= queryAncestorSelector(nodo.target, 'ul').querySelector("input[name=tema]").value;
    if(tema == ""){
        alert("Existen campos vacíos");
    }else{
        section = document.createElement('section');
        const url= `${base}/creaCuestionario`;
        const payload= {tema:tema, url_imagen:"", user_id: USER_ID};
        const request = {
        method: 'POST', 
        headers: cabeceras,
        body: JSON.stringify(payload),
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                addSection(nodo.target, tema, r.result.cuestionario_id);//Refactorización en la que añadimos el cuestionario al DOM
                console.log(`Cuestionario con tema "${tema}" añadido.`);
            }
            else{
                alert(r.error);
            }
        })
        .catch(err => alert(err));
    }
}

//borrar un tema a partir de su id y todas sus preguntas (DELETE);
function borrarCuestionario(cid) {
    event.preventDefault();
    if(cid){
        event.preventDefault();
        const url= `${base}/cuestionario/${cid}`;
        const payload= {}; 
        var request = {
        method: 'DELETE', 
        headers: cabeceras,
        body: JSON.stringify(payload),
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                console.log(`Cuestionario con el cid "${cid}" borrado.`);
            }
            else{
                alert(r.error);
            }
        })
        .catch(error => printError(error));
    }else{
        consolaError("No hay ningún cuestionario con ese CID");
    }
}

function addPregunta(nodo){
    event.preventDefault();
    const id_cuestionario = queryAncestorSelector(nodo.target, 'section').id; //Obtenemos ID de la sección
    const enunciadoPregunta = queryAncestorSelector(nodo.target, 'ul').querySelector(`input[name="${id_cuestionario + "_pregunta"}"]`).value;
    const respuestaPregunta = queryAncestorSelector(nodo.target, 'ul').querySelector(`input[name="${id_cuestionario + "_respuesta"}"]`).checked;
    
    if(!enunciadoPregunta || (respuestaPregunta == undefined || respuestaPregunta == null)){
        alert("¡Existe algún campo vacío!")
    }else{
        const url= `${base}/cuestionario/${id_cuestionario}/creaPregunta`;
        const payload= {enunciado:enunciadoPregunta, respuesta:respuestaPregunta};
        const request = {
        method: 'POST', 
        headers: cabeceras,
        body: JSON.stringify(payload),
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                addBloquePregunta(nodo.target, enunciadoPregunta, respuestaPregunta, r.result.pregunta_id);//Añadimos pregunta al DOM si está todo OK
                console.log(`Pregunta con enunciado "${enunciadoPregunta}" y respuesta "${respuestaPregunta}" añadida en el cuestionario con id "${id_cuestionario}".`);
            }
            else{
                alert(r.error);
            }
        })
        .catch(err => alert(err));
    }
}

//recuperar todos los temas (GET);
function muestraCuestionarios() {
    const url= `${base}/cuestionarios`;
    const request = {
      method: 'GET', 
      headers: cabeceras,
    };
    fetch(url,request)
    .then( response => response.json() )
    .then( r => {
        if(!r.error){
            r.result.forEach(cuestionario => {
                var section = addSection(document.querySelector("input[name='crea']"),cuestionario.tema, cuestionario.cuestionario_id);
                muestraPreguntas(cuestionario, section);//Cargamos las preguntas de cada cuestionario
            });
        }else{
            alert(r.error);
        }
    })
    .catch( error => printError(error) );
}

function muestraPreguntas(cuestionario, section) {
    if(section){
        const url= `${base}/cuestionarios/${cuestionario.cuestionario_id}/mostrarPreguntas`;
        const request = {
        method: 'GET', 
        headers: cabeceras,
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                r.result.forEach(pregunta => {
                    addBloquePregunta(section.querySelector("input[type='button'][value='Añadir nueva pregunta']"), pregunta.enunciado, pregunta.respuesta, pregunta.pregunta_id);
                });
            }else{
                alert(r.error);
            }
        })
        .catch( error => printError(error) );
    }
}

function borrarPregunta(id_pregunta) {
    event.preventDefault();
    if(id_pregunta){
        event.preventDefault();
        const url= `${base}/preguntas/${id_pregunta}/borrar`;
        const payload= {}; 
        var request = {
        method: 'DELETE', 
        headers: cabeceras,
        body: JSON.stringify(payload),
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                console.log(`Pregunta con pid "${id_pregunta}" borrada.`);
            }
            else{
                alert(r.error);
            }
        })
        .catch(error => printError(error));
    }else{
        consolaError(`No hay ninguna pregunta con id ${id_pregunta}`);
    }
}

//Borramos todas las secciones y links en la pagina
function borraSecciones(){
    const sections = document.querySelectorAll("section");
    sections.forEach(element => {
        removeElement(element);
    });
    const aList= document.querySelector("header").querySelector("nav").querySelector("ul").querySelectorAll("a");
    aList.forEach(aElement => {
        removeElement(aElement);
    });
}


//INIT
function init () {
    //Crear cuestionario al clickar
    document.querySelector("input[name='crea']").addEventListener("click", addCuestionario, false);
    muestraCuestionarios();
}

document.addEventListener('DOMContentLoaded',init,false);