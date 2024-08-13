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
        document.getElementById('scheduleData-date').value = selectedDate;
        alert("날짜가 설정되었습니다: " + selectedDate);
}

document.getElementById('post-form').addEventListener('submit', function(event) {
        event.preventDefault();  // 기본 폼 제출 방지
        savePostData();  // savePostData 함수 호출

        localStorage.removeItem('contentData');
                for (let i = 1; i <= 12; i++) {
                    const timeSlot = formatTimeSlot(i);
                    localStorage.removeItem(`check-${timeSlot}`);
                    localStorage.removeItem(`text-${timeSlot}`);
                }
    });

async function savePostData(){
    const contentData = document.getElementById('shared-textarea').value;
    const scheduleDate = document.getElementById('post-date').value;
    const token = getAccessToken();

    const PostData = {
        contentData: contentData,
        scheduleDate: scheduleDate
    };

    const response = await fetch('/api/jlaner/post/data', {
        method: 'POST',
         headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
        body: JSON.stringify(PostData)
    });
    if(response.ok){
        alert("저장되었습니다.");
    } else{
        alert("저장에 실패했습니다.");
    }

}

document.getElementById('schedule-form').addEventListener('submit', function(event) {
        event.preventDefault();  // 기본 폼 제출 방지
        saveScheduleData();  // savePostData 함수 호출

        localStorage.removeItem('contentData');
                for (let i = 1; i <= 12; i++) {
                    const timeSlot = formatTimeSlot(i);
                    localStorage.removeItem(`check-${timeSlot}`);
                    localStorage.removeItem(`text-${timeSlot}`);
                }
    });

async function saveScheduleData(){
    const scheduleDate = document.getElementById('scheduleData-date').value;
    const token = getAccessToken();

    const PostData = {
        scheduleDate: scheduleDate,
                checkBox1: document.getElementById('check-00-02').checked,
                checkBox2: document.getElementById('check-02-04').checked,
                checkBox3: document.getElementById('check-04-06').checked,
                checkBox4: document.getElementById('check-06-08').checked,
                checkBox5: document.getElementById('check-08-10').checked,
                checkBox6: document.getElementById('check-10-12').checked,
                checkBox7: document.getElementById('check-12-14').checked,
                checkBox8: document.getElementById('check-14-16').checked,
                checkBox9: document.getElementById('check-16-18').checked,
                checkBox10: document.getElementById('check-18-20').checked,
                checkBox11: document.getElementById('check-20-22').checked,
                checkBox12: document.getElementById('check-22-00').checked,
                scheduleContent1: document.getElementById('text-00-02').value,
                scheduleContent2: document.getElementById('text-02-04').value,
                scheduleContent3: document.getElementById('text-04-06').value,
                scheduleContent4: document.getElementById('text-06-08').value,
                scheduleContent5: document.getElementById('text-08-10').value,
                scheduleContent6: document.getElementById('text-10-12').value,
                scheduleContent7: document.getElementById('text-12-14').value,
                scheduleContent8: document.getElementById('text-14-16').value,
                scheduleContent9: document.getElementById('text-16-18').value,
                scheduleContent10: document.getElementById('text-18-20').value,
                scheduleContent11: document.getElementById('text-20-22').value,
                scheduleContent12: document.getElementById('text-22-00').value
    };

    const response = await fetch('/api/jlaner/schedule/data', {
        method: 'POST',
         headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
        body: JSON.stringify(PostData)
    });
    if(response.ok){
        alert("저장되었습니다.");
    } else{
        alert("저장에 실패했습니다.");
    }

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
            document.getElementById('member-name').innerText = data.memberName + '님';
        }
    } catch (error) {
        // 요청 중 오류가 발생한 경우 콘솔에 에러를 출력하고 로그인 페이지로 리디렉션
        console.error('인증 상태 확인 중 오류 발생:', error);
        window.location.href = '/login';
    }
});

document.getElementById('content-data').addEventListener('click', function(event) {
        event.preventDefault();  // 기본 폼 제출 방지
        confirmData();
    });

async function confirmData() {
    const selectedDate = document.getElementById('schedule-date').value;
    const token = getAccessToken(); // 인증 토큰 가져오기

    // 선택된 날짜의 데이터를 서버에서 요청
    try {
        const response = await fetch(`/api/jlaner/data?date=${selectedDate}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            populateScheduleData(data.scheduleData);  // 스케줄 데이터를 폼에 채우기
            populatePostData(data.postData);  // 포스트 데이터를 폼에 채우기
            alert("게시물을 가져왔습니다.");
        } else {
            alert("게시물을 가져오는 데 실패했습니다.");
        }
    } catch (error) {
        console.error('오류가 발생했습니다. ', error);
    }
}

function populateScheduleData(scheduleData) {
    document.getElementById('check-00-02').checked = scheduleData.checkBox1;
        document.getElementById('text-00-02').value = scheduleData.scheduleContent1;

        document.getElementById('check-02-04').checked = scheduleData.checkBox2;
        document.getElementById('text-02-04').value = scheduleData.scheduleContent2;

        document.getElementById('check-04-06').checked = scheduleData.checkBox3;
        document.getElementById('text-04-06').value = scheduleData.scheduleContent3;

        document.getElementById('check-06-08').checked = scheduleData.checkBox4;
        document.getElementById('text-06-08').value = scheduleData.scheduleContent4;

        document.getElementById('check-08-10').checked = scheduleData.checkBox5;
        document.getElementById('text-08-10').value = scheduleData.scheduleContent5;

        document.getElementById('check-10-12').checked = scheduleData.checkBox6;
        document.getElementById('text-10-12').value = scheduleData.scheduleContent6;

        document.getElementById('check-12-14').checked = scheduleData.checkBox7;
        document.getElementById('text-12-14').value = scheduleData.scheduleContent7;

        document.getElementById('check-14-16').checked = scheduleData.checkBox8;
        document.getElementById('text-14-16').value = scheduleData.scheduleContent8;

        document.getElementById('check-16-18').checked = scheduleData.checkBox9;
        document.getElementById('text-16-18').value = scheduleData.scheduleContent9;

        document.getElementById('check-18-20').checked = scheduleData.checkBox10;
        document.getElementById('text-18-20').value = scheduleData.scheduleContent10;

        document.getElementById('check-20-22').checked = scheduleData.checkBox11;
        document.getElementById('text-20-22').value = scheduleData.scheduleContent11;

        document.getElementById('check-22-00').checked = scheduleData.checkBox12;
        document.getElementById('text-22-00').value = scheduleData.scheduleContent12;
}

function populatePostData(postData) {
    // 서버에서 가져온 포스트 데이터를 각 폼 필드에 채운다.
    document.getElementById('shared-textarea').value = postData.contentData;
    document.getElementById('post-date').value = postData.scheduleDate;

}
