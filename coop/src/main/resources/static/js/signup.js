/**
 * 회원가입 약관 처리 및 비밀번호 일치 검증 로직
 */
const signupBtn = document.getElementById('signup-btn');
const allTerms = document.getElementById('all-terms');
const terms = document.getElementById('terms');
const privacy = document.getElementById('privacy');

// 약관 동의 체크 상태에 따라 가입 버튼 활성화/비활성화
function updateButtonState() {
  signupBtn.disabled = !(terms.checked && privacy.checked);
}

// 전체 동의 체크박스 처리
allTerms.addEventListener('change', () => {
  terms.checked = allTerms.checked;
  privacy.checked = allTerms.checked;
  updateButtonState();
});

// 개별 체크박스 변경 시 버튼 상태 업데이트
terms.addEventListener('change', updateButtonState);
privacy.addEventListener('change', updateButtonState);

// 비밀번호 일치 검증
const password = document.querySelector('input[name="password"]');
const passwordConfirm = document.querySelector('input[name="passwordConfirm"]');
passwordConfirm.addEventListener('input', () => {
  if (password.value !== passwordConfirm.value) {
    passwordConfirm.setCustomValidity('비밀번호가 일치하지 않습니다.');
  } else {
    passwordConfirm.setCustomValidity('');
  }
});