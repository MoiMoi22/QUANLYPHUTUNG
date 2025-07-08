//Toast
function showKeToanToast(message, type = 'info') {
	let toastContainer = document.getElementById('myapp-toast-container');
    
    // Kiểm tra container đã tồn tại hay chưa
    if (!toastContainer) {
        console.error('Toast container not found. Ensure #myapp-toast-container is present in the DOM.');
        return;
    }
    
    // Tạo phần tử toast
    const toast = document.createElement('div');
    toast.classList.add('myapp-toast'); // Sử dụng class namespace để tránh xung đột

    // Thêm kiểu loại thông báo
    switch (type) {
        case 'success':
            toast.classList.add('success');
            break;
        case 'error':
            toast.classList.add('error');
            break;
        case 'warning':
            toast.classList.add('warning');
            break;
        default:
            toast.style.backgroundColor = '#333'; // Kiểu mặc định nếu không xác định loại
    }
    
    // Thêm nội dung
    toast.innerHTML = `
        <i class="fas fa-info-circle"></i> 
        <span>${message}</span>
    `;
    
    // Thêm toast vào container
    toastContainer.appendChild(toast);
    
    // Xóa toast sau 4 giây
    setTimeout(() => {
        toast.remove();
    }, 4000);
    
    //Vẽ lại bảng
    //fetchDataForPage('order', 1);
}

function showKhoToast(message, type = 'info') {
	let toastContainer = document.getElementById('myapp-toast-container1');
    
    // Kiểm tra container đã tồn tại hay chưa
    if (!toastContainer) {
        console.error('Toast container not found. Ensure #myapp-toast-container is present in the DOM.');
        return;
    }
    
    // Tạo phần tử toast
    const toast = document.createElement('div');
    toast.classList.add('myapp-toast'); // Sử dụng class namespace để tránh xung đột

    // Thêm kiểu loại thông báo
    switch (type) {
        case 'success':
            toast.classList.add('success');
            break;
        case 'error':
            toast.classList.add('error');
            break;
        case 'warning':
            toast.classList.add('warning');
            break;
        default:
            toast.style.backgroundColor = '#333'; // Kiểu mặc định nếu không xác định loại
    }
    
    // Thêm nội dung
    toast.innerHTML = `
        <i class="fas fa-info-circle"></i> 
        <span>${message}</span>
    `;
    
    // Thêm toast vào container
    toastContainer.appendChild(toast);
    
    // Xóa toast sau 4 giây
    setTimeout(() => {
        toast.remove();
    }, 4000);
    
    //Vẽ lại bảng
    //fetchDataForPage('order', 1);
}