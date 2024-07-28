function httpRequest(method, url, body, success) {
    const token = localStorage.getItem('access_token');
    if (!token) {
        console.error('Access token not found in local storage');
        return;
    }
    fetch(url, {
        method: method,
        headers: {
             Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: body,
    })
    .then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            console.error('Request failed with status:', response.status);
        }
    }).catch(error => {
        console.error('Request failed with error:', error);
    });
}
const createButton = document.getElementById("create-btn");

if(createButton){
    createButton.addEventListener("click", (event) => {
    function success(){
        alert("이동합니다.");
        location.replace("/testPage");
    }
    httpRequest('GET', '/testPage', null, success);
    });
}