/*변수 선언*/
const middle = document.getElementById("middleSidebar");
const closeToggle = document.getElementById("closeToggle");
const resizer = document.getElementById("resizer");
const left = document.getElementById("leftSidebar");

let isResizing = false;
let isFloatingLocked = false;

// --- 점 3개 메뉴 토글 (공통) ---
window.currentlyOpenAction = null;
function toggleActions(el) {
	const menu = el.querySelector(".project-actions");
	if (window.currentlyOpenAction && window.currentlyOpenAction !== menu) {
		window.currentlyOpenAction.style.display = "none";
	}
	const isOpen = menu.style.display === "block";
	menu.style.display = isOpen ? "none" : "block";
	window.currentlyOpenAction = isOpen ? null : menu;
}

document.addEventListener("click", function(e) {
	const insideActions = e.target.closest(".project-actions");
	const insideDots = e.target.closest(".menu-dots");
	if (!insideActions && !insideDots && window.currentlyOpenAction) {
		window.currentlyOpenAction.style.display = "none";
		window.currentlyOpenAction = null;
	}
});


// --- 리사이징 시작 ---
resizer.addEventListener("mousedown", () => {
	isResizing = true;
	isFloatingLocked = true;
	document.body.style.cursor = "ew-resize";
});
document.addEventListener("mouseup", () => {
	isResizing = false;
	isFloatingLocked = false;
	document.body.style.cursor = "";
});
document.addEventListener("mousemove", (e) => {
	if (isResizing) {
		const newWidth = e.clientX - left.offsetWidth;
		middle.style.width = `${Math.max(180, newWidth)}px`;
		return;
	}

	// 리사이징중 마우스 하이라이트 방지
	document.addEventListener('mousedown', () => {
		document.body.style.userSelect = 'none';
	});
	document.addEventListener('mouseup', () => {
		document.body.style.userSelect = 'auto';
	});
	// 플로팅 자동 열기/닫기
	const nearLeft = e.clientX >= left.offsetWidth + 6 && e.clientX <= left.offsetWidth + 40;
	const hoverLeft = left.matches(":hover");

	if (middle.classList.contains("hidden") && nearLeft && !hoverLeft) {
		middle.classList.add("floating");
	} else if (
		middle.classList.contains("floating") &&
		(hoverLeft || !middle.matches(":hover"))
	) {
		setTimeout(() => {
			if (hoverLeft || (!left.matches(":hover") && !middle.matches(":hover"))) {
				middle.classList.remove("floating");
			}
		}, 300);
	}
});

// --- 중간 사이드바 토글 버튼 보이기/숨기기 ---
middle.addEventListener("mouseenter", () => {
	if (!middle.classList.contains("hidden")) {
		closeToggle.style.visibility = "visible";
	}
});
middle.addEventListener("mouseleave", () => {
	closeToggle.style.visibility = "hidden";
});

// --- 사이드바 숨기기 버튼 ---  
closeToggle.addEventListener("click", () => {
	middle.classList.add("hidden");
	middle.classList.remove("floating");
});


/* 프로젝트 관리 사이드바 스크립트 */

// —————————— 전역 설정 ——————————
const API_BASE = "/projects";
const listEl   = document.getElementById("project-list");

// 페이지 로드 시: 프로젝트 리스트 불러오기 + 이벤트 위임 설정
document.addEventListener("DOMContentLoaded", () => {
	loadProjects();
	setupEventDelegation();
});


// —————————— 프로젝트 리스트 로드 ——————————
function loadProjects() {
	fetch(API_BASE, { credentials: "same-origin" })
		.then(res => res.json())
		.then(projects => {
			listEl.innerHTML = "";           // 기존 목록 초기화
			projects.forEach(renderProject); // 프로젝트마다 렌더링
		})
		.catch(err => console.error("프로젝트 로드 실패", err));
}


// —————————— 이벤트 위임 설정 ——————————
function setupEventDelegation() {
	listEl.addEventListener("click", e => {
		// 1) 수정 · 삭제 버튼 클릭 먼저 처리
		const action = e.target.closest(".action-edit, .action-delete");
		if (action) {
			const tab = action.closest(".project-tab");
			const id  = tab.dataset.id;
			if (action.classList.contains("action-edit")) {
				const oldName = tab.querySelector(".project-name").textContent;
				startEdit(tab, id, oldName);
			} else {
				deleteProjectHandler(id);
			}
			return;
		}

		// 2) ⋯ 메뉴 토글은 그 다음
		const dot = e.target.closest(".menu-dots");
		if (dot) {
			toggleActions(dot);
		}
	});

	// 키다운 이벤트 (수정 input 엔터 / Esc)
	listEl.addEventListener("keydown", e => {
		if (!e.target.classList.contains("edit-input")) return;

		if (e.key === "Enter") {
			const tab     = e.target.closest(".project-tab");
			const id      = tab.dataset.id;
			const newName = e.target.value.trim();
			if (!newName) return alert("이름을 입력하세요.");
			updateProjectHandler(id, newName);
		}

		if (e.key === "Escape") {
			loadProjects();  // Esc 누르면 취소하고 전체 재로드
		}
	});
}


// —————————— 프로젝트 한 개 렌더링 ——————————
function renderProject(p) {
	const tab = document.createElement("div");
	tab.className = "project-tab d-flex justify-content-between align-items-center p-2 mb-1 rounded bg-white";
	tab.dataset.id = p.projectId;

	const nameSpan = document.createElement("span");
	nameSpan.textContent = p.projectName;
	nameSpan.className = "project-name flex-grow-1";

	const menuGroup = document.createElement("div");
	menuGroup.className = "menu-group";
	menuGroup.innerHTML = `
		<div class="dropdown-arrow">▼</div>
		<div class="menu-dots">
			<span></span><span></span><span></span>
			<div class="project-actions">
				<div class="action-edit">수정</div>
				<div class="action-delete">삭제</div>
			</div>
		</div>
	`;

	tab.append(nameSpan, menuGroup);
	listEl.append(tab);
}


// —————————— 프로젝트 추가 입력창 띄우기 ——————————
function showAddInput() {
	const existingInput = listEl.querySelector('input[type="text"]');
	if (existingInput) {
		existingInput.focus();
		return;
	}

	const input = document.createElement("input");
	input.type = "text";
	input.placeholder = "새 프로젝트명 입력";
	input.className = "form-control mb-2";
	listEl.prepend(input);
	input.focus();
	bindSaveOnEnter(input);
}

function bindSaveOnEnter(input) {
	input.addEventListener("keydown", e => {
		if (e.key !== "Enter") return;

		const name = input.value.trim();
		if (!name) return alert("프로젝트 이름을 입력하세요.");

		fetch(`${API_BASE}/add`, {
			method: "POST",
			credentials: "same-origin",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ projectName: name })
		})
			.then(res => {
				if (!res.ok) throw new Error("추가 실패");
				return res.json();
			})
			.then(() => {
				input.remove();   // 입력창 제거
				loadProjects();   // 전체 리스트 갱신
			})
			.catch(err => {
				console.error(err);
				alert(err.message);
			});
	});
}


// —————————— 수정 처리 ——————————
function startEdit(tab, id, oldName) {
	tab.innerHTML = "";
	const input = document.createElement("input");
	input.type = "text";
	input.value = oldName;
	input.className = "form-control form-control-sm flex-grow-1 edit-input";
	tab.append(input);
	input.focus();
}

function updateProjectHandler(id, newName) {
	fetch(`${API_BASE}/${id}`, {
		method: "PUT",
		credentials: "same-origin",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ projectName: newName })
	})
		.then(res => {
			if (!res.ok) throw new Error("수정 실패");
			return res.json();
		})
		.then(() => loadProjects())
		.catch(err => {
			console.error(err);
			alert(err.message);
			loadProjects();
		});
}


// —————————— 삭제 처리 ——————————
function deleteProjectHandler(id) {
	if (!confirm("정말 삭제하시겠습니까?")) return;
	fetch(`${API_BASE}/${id}`, {
		method: "DELETE",
		credentials: "same-origin"
	})
		.then(res => {
			if (!res.ok) throw new Error("삭제 실패");
			loadProjects();
		})
		.catch(err => {
			console.error(err);
			alert(err.message);
			loadProjects();
		});
}



