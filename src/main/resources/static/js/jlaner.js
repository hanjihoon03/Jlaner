function switchTab(tab) {
    // 모든 컨텐츠 탭 숨기기
    const contentTabs = document.querySelectorAll('.content-tab');
    contentTabs.forEach(tab => tab.classList.remove('active'));

    // 선택된 탭 표시
    document.getElementById(`${tab}-tab`).classList.add('active');
}

const sharedTextarea = document.getElementById('shared-textarea');
const youtubeContainer = document.getElementById('youtube-container');
const youtubePreview = document.getElementById('youtube-preview');

function insertYouTubeVideo() {
    const url = document.getElementById('youtube-url').value;

    // URL에서 비디오 ID 추출
    let videoId = url.split('v=')[1];
    const ampersandPosition = videoId ? videoId.indexOf('&') : -1;
    if (ampersandPosition !== -1) {
        videoId = videoId.substring(0, ampersandPosition);
    }

    // YouTube 미리보기 iframe 업데이트
    youtubePreview.src = `https://www.youtube.com/embed/${videoId}`;

    // YouTube 미리보기 표시
    youtubeContainer.style.display = 'block';
}

function toggleYouTubeContainer() {
    if (youtubeContainer.style.display === 'none') {
        youtubeContainer.style.display = 'block';
        document.getElementById('youtube-link-input').style.marginTop = '0px'; // 텍스트 영역에 대한 여백 조정
    } else {
        youtubeContainer.style.display = 'none';
        document.getElementById('youtube-link-input').style.marginTop = '0'; // 텍스트 영역에 대한 여백 초기화
    }
}

function confirmDate() {
        const selectedDate = document.getElementById('schedule-date').value;
        document.getElementById('post-date').value = selectedDate;
        document.getElementById('schedule-date-hidden').value = selectedDate;
        alert("날짜가 설정되었습니다: " + selectedDate);
}


document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 인증 상태를 확인하기 위해 '/api/check-auth' 엔드포인트로 요청을 보냄
        const response = await fetchWithAuth('/api/check-auth');

        // 응답이 성공적이지 않은 경우, 예: 인증 실패
        if (!response.ok) {
            // 에러 처리, 예: 로그인 페이지로 리디렉션
            window.location.href = '/login';
        } else {
            // 응답을 JSON으로 변환
            const data = await response.json();
        }
    } catch (error) {
        // 요청 중 오류가 발생한 경우 콘솔에 에러를 출력하고 로그인 페이지로 리디렉션
        console.error('인증 상태 확인 중 오류 발생:', error);
        window.location.href = '/login';
    }
});