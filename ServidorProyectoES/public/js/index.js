const cabeceras= {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
}

function submitLogin(event){
    event.preventDefault();
    const email = document.querySelector(`input[name=email]`).value;
    const password = document.querySelector(`input[name=password]`).value;

    if(!password || !email){
        alert("¡Existe algún campo vacío!")
    }else{
        const url= `/login`;
        const payload= {email: email, password: password};
        const request = {
        method: 'POST', 
        headers: cabeceras,
        body: JSON.stringify(payload),
        };
        fetch(url,request)
        .then( response => response.json() )
        .then( r => {
            if(!r.error){
                window.location.replace('/menu');
            }
            else{
                alert(r.error);
            }
        })
        .catch(err => alert(err));
    }
}

function init () {
    document.querySelector("button[id='button_login']").addEventListener("click", submitLogin, false);
}

document.addEventListener('DOMContentLoaded',init,false);