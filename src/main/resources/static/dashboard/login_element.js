//const role = localStorage.getItem('role');; // Giá trị của biến role
// Đảm bảo rằng bạn đã đăng nhập trước khi gọi API
var role = "";
fetch('/api/role', {
    method: 'GET',  // Phương thức HTTP GET
    headers: {
        'Content-Type': 'application/json',  // Đảm bảo gửi đúng loại nội dung
    }
})
.then(response => {
    if (!response.ok) {
        throw new Error('Failed to fetch role');
    }
    return response.text();  // Giải mã JSON từ response
})
.then(role => {
	if (role === "ROLE_ACCOUNTANT") {
	    // Ẩn mục Dashboard
	    document.querySelector('a.sidebar-link[href="/dashboard"]').closest('li').style.display = "none";
	
	    // Ẩn mục Người Dùng
	    document.querySelector('a.sidebar-link[href="/user"]').closest('li').style.display = "none";
	
	    // Ẩn mục Phụ Tùng
	    document.querySelector('a.sidebar-link[href="/product"]').closest('li').style.display = "none";
	    
	    // Thêm mục "Thanh Toán Hóa Đơn" lên trên mục "Hóa Đơn"
	    const orderElement = document.querySelector('a.sidebar-link[href="/order"]').closest('li');
	    const newElement = document.createElement("li");
	    newElement.className = "sidebar-item";
	    newElement.innerHTML = `
	        <a class="sidebar-link" href="/accountant" aria-expanded="false">
	            <span>
	                <i class="bi bi-wallet2"></i>
	            </span>
	            <span class="hide-menu">Thanh Toán Hóa Đơn</span>
	        </a>
	    `;
	    // Chèn thẻ mới lên trên mục "Hóa Đơn"
	    orderElement.parentNode.insertBefore(newElement, orderElement);
	    
	    // Đổi nội dung của thẻ "Quản lý" thành "KẾ TOÁN"
	    const managementElement = document.querySelector('.nav-small-cap .hide-menu');
	    if (managementElement) {
	        managementElement.textContent = "KẾ TOÁN";
	    }
	}
	else {
		
		if ($('#btnAddReceive').length) {
		    $('#btnAddReceive').parent().hide();
		}

		if ($('#btnAddSpend').length) {
		    $('#btnAddSpend').parent().hide();
		}
		
		if ($('#btnAddPurchase').length) {
		    $('#btnAddPurchase').parent().hide();
		}
	}
	
	if ($('#btnAddOrder').length) {
	    $('#btnAddOrder').parent().hide();
	}    
})
.catch(error => {
    console.error('Error fetching role:', error);
});


