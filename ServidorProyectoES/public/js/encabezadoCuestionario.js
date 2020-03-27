(function() {
    const template = document.createElement('template');
    template.innerHTML = `<style>wiki{font-size: 90%;}h2{font-size: 25px;}img{width: 50px; margin-right: 10px; vertical-align: text-top; border: solid 1px;border-color: lightgray;}</style>
    <h2><img alt="Vacío"><span> Cuestionario sobre </span></h2>`;
    
    //PRACTICA 3
    function addWikipedia(terminoB, nodo){
        fetch('https://es.wikipedia.org/w/api.php?origin=*&format=json&action=query&prop=extracts&exintro&explaintext&continue&titles=' + terminoB)
        .then(function(response) {
            if (!response.ok) {
                throw Error(response.statusText);
            }
            return response.json();
        })
        .then(function(responseAsJson) {
            var texto = "";
            let page = responseAsJson.query.pages;
            Object.keys(responseAsJson.query.pages).forEach(pageId => {//Recorremos las claves
                if(pageId != -1){//Comprobamos que existe alguna página antes de añadir texto
                    texto += page[pageId].extract;
                }
            });
            divWiki = document.createElement('div');
            divWiki.className = 'wiki';
            if(texto != ""){//Añadimos el texto sólo si hay algo que escribir
                var regex = /\[.[0-9]?\]/g;//Expresión regular corchete . seguido de numero y además cierra corchete
                var replacement = "";//Reemplazo 
                divWiki.textContent = texto.replace(regex, replacement);//Función de reemplazo
            }
            insertBeforeChild(nodo, nodo.querySelector('div.formulario'), divWiki); //Insertamos el texto entre h2 y el formulario
        })
        .catch(function(error) {
            console.log('Ha habido un problema: ', error);
        });    
    }

    function addFlickr(terminoB, nodoImg){
        const api_key = "0dc6901015dd171b36803ccf9ab12926";
        nodoImg.setAttribute("alt", "Vacío"); 
        fetch(`https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=${api_key}&text=${terminoB}&format=json&per_page=10&media=photos&sort=relevance&nojsoncallback=1`)//Cargamos una lista de fotos relacionadas
        .then(function(response) {
            if (!response.ok) {
                throw Error(response.statusText);
            }
            return response.json();
        })
        .then(function(responseAsJson) {
            let photo = responseAsJson.photos.photo[0];//Cogemos la primera imagen
            if(photo){//Existe la imagen
                fetch(`https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=${api_key}&photo_id=${photo.id}&format=json&nojsoncallback=1`)//Cargamos la info de la foto seleccionada
                .then(function(response) {
                    if (!response.ok) {
                        throw Error(response.statusText);
                    }
                    return response.json();
                })
                .then(function(responseAsJson) {
                    nodoImg.setAttribute("src", responseAsJson.sizes.size[0].source);//Cargamos la primera imagen
                    
                });
            }else{//No existe la imagen
                nodoImg.setAttribute("src", "https://eoimages.gsfc.nasa.gov/images/imagerecords/57000/57723/globe_east_540.jpg");//Cargamos imagen de la tierra
            }
        })
        .catch(function(error) {
            console.log('Ha habido un problema: ', error);
        });   
    }
  
    class Encabezado extends HTMLElement {
        constructor() {
            super();
            let tclone = template.content.cloneNode(true);
            let shadowRoot = this.attachShadow({
                mode: 'open' 
            });
            shadowRoot.appendChild(tclone);
        }
  
        connectedCallback() {
            if(this.hasAttribute('tema')){//Si se introduce tema en el input
                let tema = this.getAttribute('tema');
                this.shadowRoot.querySelector('span').textContent += tema;//Añadimos el tema al título
                addFlickr(tema, this.shadowRoot.querySelector('img'));
                addWikipedia(tema, this.shadowRoot);//Pasamos como nodo todo el shadowRoot para añadir el div al final del encabezado
            }else{//Si está vacío
                console.log("No existe tema en el encabezado");
            }
        }
    }
  
    customElements.define("encabezado-cuestionario", Encabezado); //Definimos el nombre del componente
  
  })();