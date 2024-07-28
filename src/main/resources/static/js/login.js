// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    // fetch를 사용하여 HTTP 요청을 보냅니다.
    fetch(url, {
        method: method, // HTTP 메서드 (GET, POST, PUT, DELETE 등)
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'), // Authorization 헤더에 Bearer 토큰 추가
            'Content-Type': 'application/json', // Content-Type을 JSON으로 설정
        },
        body: body, // 요청의 본문 (JSON 문자열)
    }).then(response => {
        // 응답이 성공적일 경우 (200 또는 201)
        if (response.status === 200 || response.status === 201) {
            return success(); // 성공 콜백 함수 호출
        }
        // 응답이 401 (Unauthorized)이고 리프레시 토큰이 있는 경우
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            // 새로운 엑세스 토큰을 얻기 위해 /api/token 엔드포인트로 POST 요청
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'), // Authorization 헤더에 현재 액세스 토큰 추가
                    'Content-Type': 'application/json', // Content-Type을 JSON으로 설정
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'), // 요청 본문에 리프레시 토큰 추가
                }),
            })
                .then(res => {
                    // 토큰 재발급 요청이 성공적일 경우 (응답이 OK)
                    if (res.ok) {
                        return res.json(); // 응답을 JSON으로 변환하여 반환
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지 값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken); // 새로운 액세스 토큰을 로컬 스토리지에 저장
                    httpRequest(method, url, body, success, fail); // 원래 요청을 다시 시도
                })
                .catch(error => fail()); // 재발급 요청이 실패하면 실패 콜백 함수 호출
        } else {
            return fail(); // 다른 상태 코드이거나 리프레시 토큰이 없으면 실패 콜백 함수 호출
        }
    });
}
