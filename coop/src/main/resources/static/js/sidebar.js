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

/*프로젝트 */

// --- 프로젝트 추가 입력창 띄우기 ---
function showAddInput() {
	const list = document.getElementById("project-list");
	const existingInput = list.querySelector('input[type="text"]');
	if (existingInput) {
		existingInput.focus();
		return;
	}

	const input = document.createElement("input");
	input.type = "text";
	input.placeholder = "새 프로젝트명 입력";
	input.className = "form-control mb-2";
	list.prepend(input);
	input.focus();
	bindSaveOnEnter(input, list);
}

function bindSaveOnEnter(input, list) {
	input.addEventListener("keydown", e => {
		if (e.key !== "Enter") return;

		const name = input.value.trim();
		if (!name) return alert("프로젝트 이름을 입력하세요.");

		fetch("/projects/add", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ projectName: name })
		})
			.then(res => {
				if (!res.ok) throw new Error("추가 실패");
				return res.json();

			})
			.then(data => {
				// 1) 화면에 새 탭 추가
				const tab = document.createElement("div");
				tab.className = "project-tab d-flex justify-content-between align-items-center p-2 mb-1 rounded bg-white";
				tab.textContent = data.projectName;
				tab.dataset.id = data.projectId;
				list.insertBefore(tab, input.nextSibling);

				// 2) input 제거 후 포커스 해제
				input.remove();
			})
			.catch(err => {
				console.error(err);
				alert(err.message);
			});
	});
}



// 엔드포인트 상수
const API_BASE = "/projects";

// DOM 요소
const listEl = document.getElementById("project-list");

// 페이지 로드 시 프로젝트 불러오기
document.addEventListener("DOMContentLoaded", loadProjects);

function loadProjects() {
	fetch(API_BASE, { credentials: "same-origin" })
		.then(res => res.json())
		.then(projects => {
			listEl.innerHTML = "";  // 초기화
			projects.forEach(renderProject);
		})
		.catch(err => console.error("로드 실패", err));
}

// 한 프로젝트를 렌더링
function renderProject(p) {
  // 1) 프로젝트 탭 컨테이너
  const tab = document.createElement("div");
  tab.className = "project-tab d-flex justify-content-between align-items-center p-2 mb-1 rounded bg-white";
  tab.dataset.id = p.projectId;

  // 2) 프로젝트 이름
  const nameSpan = document.createElement("span");
  nameSpan.textContent = p.projectName;
  nameSpan.className = "flex-grow-1";

  // 3) ⋯ 메뉴 그룹
  const menuGroup = document.createElement("div");
  menuGroup.className = "menu-group";

  // 3-1) ▼ 화살표
  const arrow = document.createElement("div");
  arrow.className = "dropdown-arrow";
  arrow.textContent = "▼";

  // 3-2) 점 3개
  const dots = document.createElement("div");
  dots.className = "menu-dots";
  dots.onclick = () => toggleActions(dots);

  // 점 3개 span
  for (let i = 0; i < 3; i++) {
    const span = document.createElement("span");
    dots.append(span);
  }

  // 3-3) project-actions (숨겨진 수정/삭제 메뉴)
  const actions = document.createElement("div");
  actions.className = "project-actions";

  const edit = document.createElement("div");
  edit.textContent = "수정";
  edit.onclick = () => startEdit(tab, p.projectId, p.projectName);

  const del = document.createElement("div");
  del.textContent = "삭제";
  del.onclick = () => deleteProject(tab, p.projectId);

  actions.append(edit, del);
  dots.append(actions);

  // 3-4) 메뉴 그룹 조립
  menuGroup.append(arrow, dots);

  // 4) 탭에 이름 + 메뉴 추가
  tab.append(nameSpan, menuGroup);

  // 5) 리스트에 붙이기
  listEl.append(tab);
}

// 편집 시작: input 으로 교체
function startEdit(tab, id, oldName) {
	tab.innerHTML = "";
	const input = document.createElement("input");
	input.type = "text";
	input.value = oldName;
	input.className = "form-control form-control-sm flex-grow-1";
	tab.append(input);

	input.focus();
	input.addEventListener("keydown", e => {
		if (e.key === "Enter") {
			const newName = input.value.trim();
			if (!newName) return alert("이름을 입력하세요.");
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
				.then(updated => {
					// 변경된 이름으로 다시 그리기
					renderProject(updated);
				})
				.catch(err => {
					console.error(err);
					alert(err.message);
					loadProjects();
				});
		}
	});
}

// 삭제
function deleteProject(tab, id) {
	if (!confirm("정말 삭제하시겠습니까?")) return;
	fetch(`${API_BASE}/${id}`, {
		method: "DELETE",
		credentials: "same-origin"
	})
		.then(res => {
			if (!res.ok) throw new Error("삭제 실패");
			tab.remove();
		})
		.catch(err => {
			console.error(err);
			alert(err.message);
		});
}

