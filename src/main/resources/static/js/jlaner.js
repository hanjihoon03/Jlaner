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
    alert(`선택된 날짜: ${selectedDate}`);
}
