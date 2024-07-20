function switchTab(tab) {
    // Hide all content tabs
    const contentTabs = document.querySelectorAll('.content-tab');
    contentTabs.forEach(tab => tab.classList.remove('active'));

    // Show the selected tab
    document.getElementById(`${tab}-tab`).classList.add('active');
}

const sharedTextarea = document.getElementById('shared-textarea');
const youtubeContainer = document.getElementById('youtube-container');
const youtubePreview = document.getElementById('youtube-preview');

function insertYouTubeVideo() {
    const url = document.getElementById('youtube-url').value;

    // Extract video ID from URL
    let videoId = url.split('v=')[1];
    const ampersandPosition = videoId ? videoId.indexOf('&') : -1;
    if (ampersandPosition !== -1) {
        videoId = videoId.substring(0, ampersandPosition);
    }

    // Update YouTube preview iframe
    youtubePreview.src = `https://www.youtube.com/embed/${videoId}`;

    // Show YouTube preview
    youtubeContainer.style.display = 'block';
}

function toggleYouTubeContainer() {
    if (youtubeContainer.style.display === 'none') {
        youtubeContainer.style.display = 'block';
        document.getElementById('youtube-link-input').style.marginTop = '0px'; // Adjusted margin for textarea
    } else {
        youtubeContainer.style.display = 'none';
        document.getElementById('youtube-link-input').style.marginTop = '0'; // Reset margin for textarea
    }
}

function confirmDate() {
    const selectedDate = document.getElementById('schedule-date').value;
    alert(`Selected date: ${selectedDate}`);
}