function alertError() {
            const params = new URLSearchParams(window.location.search);
            const error = params.get('error');

            if (error) {
                let message = '';

                switch (error) {
                    case 'memberNotFound':
                        message = '회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.';
                        break;
                    case 'invalidRefreshToken':
                        message = '유효하지 않은 리프레시 토큰입니다. 다시 로그인해주세요.';
                        break;
                    case 'missingRefreshToken':
                        message = '리프레시 토큰이 없습니다. 다시 로그인해주세요.';
                        break;
                    case 'unauthorized':
                        message = '인증되지 않은 요청입니다. 다시 로그인해주세요.';
                        break;
                }

                alert(message);
            }
}

// 페이지가 로드될 때 showAlertFromQuery 함수를 호출
window.onload = alertError;