
// Lưu trạng thái số trang của từng bảng
const pageStates = {
    receive: 1,
    spend: 1,
};
const isFinal = {
    receive: false,
    spend: false,
};

// Get modal and buttons
const deleteModal = document.getElementById("deleteModal");
const closeModal = document.getElementById("closeModal");
const cancelBtn = document.getElementById("cancelBtn");
const confirmDelete = document.getElementById("confirmDelete");

// Handle delete icon click
const deleteButtons = document.querySelectorAll('.delete');
let deleteId = null;

deleteButtons.forEach(button => {
    button.addEventListener("click", (e) => {
        e.preventDefault();
        // Get customer ID from data-id attribute
        deleteId = e.target.closest("a").getAttribute('data-id');
        // Open modal
        deleteModal.style.display = "block";
    });
});

// Close modal
closeModal.addEventListener("click", () => {
    deleteModal.style.display = "none";
});

cancelBtn.addEventListener("click", () => {
    deleteModal.style.display = "none";
});

// Hàm gắn sự kiện cho các nút Delete
function attachDeleteEvents() {
    const deleteButtons = document.querySelectorAll('.delete');
    deleteButtons.forEach(button => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            // Lấy ID người dùng từ thuộc tính data-id
            deleteId = e.target.closest("a").getAttribute('data-id');
            // Mở modal xác nhận xóa
            deleteModal.style.display = "block";
        });
    });
}

// Xử lý sự kiện xác nhận xóa
confirmDelete.addEventListener("click", () => {
    const prefix = deleteId.slice(0, 7).trim(); // Lấy prefix từ ID
    const id = deleteId.slice(7);             // Lấy ID người dùng
    const deleteUrl = `http://localhost:8080/transaction/delete/${id}`;
    const getAllUrl = `http://localhost:8080/transaction/${prefix}/all/`+pageStates[`${prefix}`];

    fetch(deleteUrl, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("HTTP error! Status: " + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                showNotificationModal("Xóa thành công", `Người dùng với ID ${data.id} đã được xóa.`);
                return fetch(getAllUrl);
            } else {
                throw new Error(data.message);
            }
        })
        .then(response => response.json())
        .then(users => {
            const tableId = deleteId.slice(0, 7).trim() + "Table";
			renderTable(tableId, users.users);
        })
        .catch(error => {
            console.error("Error:", error);
            showNotificationModal("Xóa thất bại", error.message);
        })
        .finally(() => {
            deleteModal.style.display = "none";
        });
});

// Gắn sự kiện ban đầu cho các nút Delete khi DOM được load
document.addEventListener("DOMContentLoaded", () => {
    attachDeleteEvents();
});

// Hàm hiển thị modal thông báo
function showNotificationModal(title, message) {
    const successModal = document.getElementById("succesModal");
    const modalContent = successModal.querySelector(".modal-content");

    // Cập nhật nội dung modal
    modalContent.innerHTML = `
        <span class="close" id="closeSuccessModal">&times;</span>
        <h2>${title}</h2>
        <p>${message}</p>
        <div class="modal-actions">
            <button id="closeSuccessBtn" class="cancel-button">Đóng</button>
        </div>
    `;

    // Hiển thị modal
    successModal.style.display = "block";

    // Gắn sự kiện đóng modal
    document.getElementById("closeSuccessBtn").addEventListener("click", () => {
        successModal.style.display = "none";
    });

    document.getElementById("closeSuccessModal").addEventListener("click", () => {
        successModal.style.display = "none";
    });
}

function renderTable(tableId, users) {
    const tbody = document.querySelector(`#${tableId} tbody`);
    console.log(tableId+'dung');
    tbody.innerHTML = ""; // Xóa nội dung cũ
    
    if (tableId.includes("receive")) {
        users.forEach((transaction, index) => {
            tbody.innerHTML += `
	            <tr>
	                <td>${index + 1}</td> <!-- Số thứ tự -->
	                <td>${transaction.transactionId}</td>
	                <td>${transaction.transactionDate}</td>
	                <td>${transaction.transactionType}</td>
	                <td>${transaction.amount.toFixed(2)}</td>
	                <td>${transaction.title}</td>
	                <td>${transaction.relatedOrderId || 'N/A'}</td> <!-- Mã đơn có thể không tồn tại -->
	                <td>
	                    <ul class="action-list list-inline">
	                        <li class="list-inline-item">
	                            <a href="#" class="view" data-tip="Xem"><i class="fas fa-eye"></i></a>
	                        </li>
	                        <li class="list-inline-item">
	                            <a href="#" class="edit" data-tip="Chỉnh Sửa"><i class="fas fa-pencil-alt"></i></a>
	                        </li>
	                        <li class="list-inline-item">
	                            <a href="#" class="delete" data-tip="Xóa" data-id="receive ${transaction.transactionId}">
	                                <i class="fas fa-trash"></i>
	                            </a>
	                        </li>
	                    </ul>
	                </td>
	            </tr>
	        `;
	    });
    } else if (tableId.includes("spend")) {
        users.forEach((transaction, index) => {
            tbody.innerHTML += `
	            <tr>
	                <td>${index + 1}</td> <!-- Số thứ tự -->
	                <td>${transaction.transactionId}</td>
	                <td>${transaction.transactionDate}</td>
	                <td>${transaction.transactionType}</td>
	                <td>${transaction.amount.toFixed(2)}</td>
	                <td>${transaction.title}</td>
	                <td>${transaction.relatedOrderId || 'N/A'}</td> <!-- Mã đơn có thể không tồn tại -->
	                <td>
	                    <ul class="action-list list-inline">
	                        <li class="list-inline-item">
	                            <a href="#" class="view" data-tip="Xem"><i class="fas fa-eye"></i></a>
	                        </li>
	                        <li class="list-inline-item">
	                            <a href="#" class="edit" data-tip="Chỉnh Sửa"><i class="fas fa-pencil-alt"></i></a>
	                        </li>
	                        <li class="list-inline-item">
	                            <a href="#" class="delete" data-tip="Xóa" data-id="spend ${transaction.transactionId}">
	                                <i class="fas fa-trash"></i>
	                            </a>
	                        </li>
	                    </ul>
	                </td>
	            </tr>
	        `;
	    });

    } 

    // Gắn lại sự kiện cho các nút Delete
    attachDeleteEvents();
}

// Hàm cập nhật số trang
function updatePage(table, newPage) {
	//Cập nhật số trang
    pageStates[table] = newPage;
    //isFinal[table] = "[[${isFinal}]]";
    const pagination = document.querySelector(`.pagination[data-table="${table}"]`);
    const currentPageElem = pagination.querySelector("#currentPage a");
    currentPageElem.textContent = newPage;
    

    // Vẽ lại bảng
    // Xây dựng selector dựa trên biến role
	const searchInput = document.querySelector(`.form-control.search-input.${table}`);
	
	// Mode search
	if (searchInput.value.trim() === '') {
	    fetchDataForPage(table, newPage);
	} 
	// Mode bình thường
	else {
		fetchDataForSearch(table, searchInput.value, newPage)
	    console.log(`The input has value: ${searchInput.value}`);
	}
}

//Xử lý paging
document.addEventListener("DOMContentLoaded", () => {


    // Gắn sự kiện click cho các nút phân trang
    document.querySelectorAll(".pagination").forEach((pagination) => {
        const table = pagination.dataset.table;

        pagination.addEventListener("click", (e) => {
            const target = e.target.closest("li");
            if (!target) return;

            e.preventDefault();
            //Phân biệt sang trang mode search hay all
            //mode = 
            if (target.classList.contains("prevPage")) {
                if (pageStates[table] > 1) {
                    updatePage(table, pageStates[table] - 1);
                }
            } else if (target.classList.contains("nextPage") && isFinal[table] === false) {
                updatePage(table, pageStates[table] + 1);
            }
        });
    });
});

function fetchDataForPage(table, page) {
    const apiUrl = `http://localhost:8080/transaction/${table}/all/${page}`;
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
			isFinal[table] = data.isFinal;
            renderTable(`${table}Table`, data.users);
        })
        .catch(error => console.error("Lỗi khi tải dữ liệu:", error));
}

//Tìm kiếm
// Lấy tất cả các input có class 'search-input'
const searchInputs = document.querySelectorAll('.search-input');
console.log(searchInputs+'dung');

// Lặp qua từng input và gán sự kiện 'input' (xử lý khi nội dung thay đổi)
searchInputs.forEach((input, index) => {
    input.addEventListener('input', (event) => {
		//console.log( `${event.target.value}`+index);
		//const input_search = `${event.target.value}`;
        //Tìm kiếm khách hàng
        if (index === 0){
			updatePage('receive', 1);
		}
		//Tìm kiếm nhân viên
		else if(index === 1){
			updatePage('spend', 1);
		}
    });
});

function fetchDataForSearch(table, input_search, page) {
    const apiUrl = `http://localhost:8080/transaction/${table}/search/${input_search}/${page}`;
    console.log(apiUrl);
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
			isFinal[table] = data.isFinal;
            renderTable(`${table}Table`, data.users);
        })
        .catch(error => console.error("Lỗi khi tải dữ liệu:", error));
}
