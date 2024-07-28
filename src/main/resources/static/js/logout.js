document.getElementById('logout-button').addEventListener('click', function() {
    fetch('/logout', {
        method: 'POST',
        credentials: 'include' // 쿠키 포함
    }).then(response => {
        if (response.ok) {
            localStorage.removeItem("access_token");
            window.location.href = '/login'; // 로그아웃 후 로그인 페이지로 리다이렉션
        } else {
            console.error('Logout failed');
        }
    }).catch(error => {
        console.error('Logout error:', error);
    });
});