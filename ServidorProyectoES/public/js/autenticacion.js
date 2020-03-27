  var USER_ID = null;

  function loginFirst (event) {
    event.preventDefault();
    alert("Identifícate antes de usar la aplicación")
  }

  // ---- código que se encarga de la identificación del usuario

  var id_token= null; // token a enviar al servidor

  // id de cliente obtenido en la consola web de Google Cloud Platform:
  const clientId= "806929042135-1paio8ol9ma2e9riqs1bnteqi6lpmgej.apps.googleusercontent.com";

  // función que se invoca cuando la librería de Google está cargada; el nombre de esta función
  // se pasa como parámetro al elemento script:
  function initGoogleAPI() { 
    // inicializa el objeto GoogleAuth:
    gapi.load('auth2', function(){
      auth2 = gapi.auth2.init({
          client_id: clientId,
          scope: 'profile email'
      });
      const func= 'initGoogleAPI';
      document.querySelector('#usuario').textContent= '';
      // al principio se muestra el botón de identificación y se oculta el de cerrar sesión:
      document.querySelector('#salir').classList.add('invisible');
      document.querySelector('#entrar').classList.remove('invisible');

      // registra el botón que permite iniciar el proceso de autenticación y los funciones
      // callback correspondientes:
      auth2.attachClickHandler('entrar', {}, onSuccessSignIn, onFailureSignIn);

      // registra la función callback que se invoca cuando cambia el estado de logueado/no logueado del usuario:
      auth2.isSignedIn.listen(signinChanged);

      // registra la función callback que se llama cada vez que cambia el valor de currentUser:
      auth2.currentUser.listen(userChanged);
    });
  }

  // se ejecuta cuando cambia el estado de login del usuario:
  var signinChanged = function(val) {
    const func= 'signinChanged';
    USER_ID = null;
    if (val) {
      // token que se enviará al servidor para que lo valide e identifique al usuario:
      id_token= auth2.currentUser.get().getAuthResponse().id_token;
      // se añade a las cabeceras de las peticiones al servidor el token de autenticación:
      cabeceras["Authorization"]= `Bearer ${id_token}`;
      USER_ID = `${auth2.currentUser.get().getBasicProfile().U3}`;
      document.querySelector('#usuario').textContent= `${USER_ID} `;
      document.querySelector('#identificado').textContent= "Identificado como:";
      document.querySelector('#salir').classList.remove('invisible');
      document.querySelector('#entrar').classList.add('invisible');
    }
    else {
      cabeceras["Authorization"]= '';
      document.querySelector('#usuario').textContent= '';
      document.querySelector('#salir').classList.add('invisible');
      document.querySelector('#entrar').classList.remove('invisible');
      document.querySelector('#identificado').textContent= "Sin identificar";
    }
    //Recargamos los cuestionarios
    borraSecciones();
    muestraCuestionarios();
  };

  // esta función se invoca cuando el usuario se loguea tras pulsar el botón; si el usuario estaba identificado
  // anteriormente y se recarga la página, esta función no se invoca (no se ha pulsado el botón), pero la
  // librería intenta identificar automáticamente al usuario y, si lo consigue, sí se invoca 'signinChanged' y
  // 'userChanged':
  function onSuccessSignIn(user) {

  };

  // función de callback cuando el usuario no puede loguearse:
  function onFailureSignIn(error) {
    id_token= null;
    const func= 'onFailureSignIn';
  };

  // manejador del evento de hacer clic en el botón de salir de la sesion:
  function signOut() {
    auth2.signOut().then(function () {
      const func= 'signOut';
      id_token= null;
      cabeceras["Authorization"]= '';
      USER_ID = null;
    });
  }        

  // función de callback invocada cuando cambia el usuario logueado:
  function userChanged(user) {
    if(user.getId()){
      const func= 'userChanged';
      var profile = user.getBasicProfile();
    }
  };

  // función de inicialización:
  function initAuth () {
    let e= document.querySelector('#f0');
    // si no hay token id, el manejador es 'loginFirst'; si no, usa 'envíaMensaje'
    //e.addEventListener('submit',e => id_token?envíaMensaje(e):loginFirst(e),false);
    e= document.querySelector("#salir");
    e.addEventListener('click',signOut,false);
  }

  document.addEventListener('DOMContentLoaded',initAuth,false);