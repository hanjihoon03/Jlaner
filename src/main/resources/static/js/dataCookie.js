// 페이지를 떠나기 전에 데이터를 로컬 스토리지에 저장
window.addEventListener('beforeunload', function(event) {
    // 텍스트 영역의 내용을 로컬 스토리지에 저장
    const contentData = document.getElementById('shared-textarea').value;
    localStorage.setItem('contentData', contentData);

    // 스케줄 영역의 각 항목을 로컬 스토리지에 저장
    for (let i = 1; i <= 12; i++) {
        const timeSlot = formatTimeSlot(i);
        const checkbox = document.getElementById(`check-${timeSlot}`).checked;
        const textarea = document.getElementById(`text-${timeSlot}`).value;

        // 각 항목을 개별적으로 저장
        localStorage.setItem(`check-${timeSlot}`, checkbox);
        localStorage.setItem(`text-${timeSlot}`, textarea);
    }
});

// 시간을 포맷하는 함수 (2시간 간격)
function formatTimeSlot(index) {
    const startHour = (index - 1) * 2;
    const endHour = startHour + 2;
    return `${String(startHour).padStart(2, '0')}-${String(endHour).padStart(2, '0')}`;
}

// 페이지 로드 시 로컬 스토리지에서 데이터를 복원
document.addEventListener('DOMContentLoaded', function() {
    // 복원 여부를 묻는 알림
    const restoreData = confirm("이전 데이터를 복원하시겠습니까?");

    if (restoreData) {
        // 텍스트 영역의 내용을 로컬 스토리지에서 복원
        const contentData = localStorage.getItem('contentData');
        if (contentData) {
            document.getElementById('shared-textarea').value = contentData;
        }

        // 스케줄 영역의 각 항목을 로컬 스토리지에서 복원
        for (let i = 1; i <= 12; i++) {
            const timeSlot = formatTimeSlot(i);
            const checkboxValue = localStorage.getItem(`check-${timeSlot}`);
            const textareaValue = localStorage.getItem(`text-${timeSlot}`);

            // 복원된 값이 있는 경우에만 해당 요소에 설정
            if (checkboxValue !== null) {
                document.getElementById(`check-${timeSlot}`).checked = (checkboxValue === 'true');
            }

            if (textareaValue !== null) {
                document.getElementById(`text-${timeSlot}`).value = textareaValue;
            }
        }
    }
});
