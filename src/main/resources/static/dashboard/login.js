/*document.querySelector('.login100-form-btn1').addEventListener('click', async (event) => {
    event.preventDefault(); // Ngăn hành động mặc định của nút

    // Lấy giá trị từ các trường input
    const username = document.querySelector('input[name="username"]').value;
    const password = document.querySelector('input[name="password"]').value;

    // Kiểm tra xem các trường có được điền hay không
    if (!username || !password) {
        alert('Vui lòng điền đầy đủ email và mật khẩu.');
        return;
    }

    // Dữ liệu gửi về server
    const loginData = {
        username: username,
        password: password,
    };

    try {
        // Gửi yêu cầu đến server
        const response = await fetch('/api/auth/login', {
            method: 'POST', // Phương thức gửi
            headers: {
                'Content-Type': 'application/json', // Dữ liệu là JSON
            },
            body: JSON.stringify(loginData), // Chuyển dữ liệu thành chuỗi JSON
        });

        // Xử lý phản hồi từ server
        if (response.ok) {
            const result = await response.json();
            //alert('Đăng nhập thành công!');
            console.log(result);
			
            // Lưu role vào local storage
            role = result.role.map(item => item.authority);
            localStorage.setItem('role', role);

            // Lưu token vào local storage
            const token = result.token;
            localStorage.setItem('token', token);
						
            // Chuyển hướng đến trang chính sau khi đăng nhập thành công
            if(role === "ROLE_MANAGER")
				fetch('dashboard', {
				    method: 'GET',
				    headers: {
				        'Authorization': `Bearer ${token}`
				    }
				})
            else if (role === "ROLE_ACCOUNTANT")
				fetch('ketoan', {
				    method: 'GET',
				    headers: {
				        'Authorization': `Bearer ${token}`
				    }
				})
            else 
				console.log(`Bearer ${token}`);
				fetch('/home', {
				    method: 'GET',
				    headers: {
				        'Authorization': `Bearer ${localStorage.getItem('token')}`
				    }
				}).then(response =>{
					window.location.href = '/home';
				})
			
        } else {
            const error = await response.json();
            alert('Đăng nhập thất bại: ' + error.message);
        }
    } catch (error) {
        console.error('Đã xảy ra lỗi:', error);
        alert('Có lỗi trong quá trình đăng nhập.');
    }
});*/

document.addEventListener('DOMContentLoaded', function () {
  const urlParams = new URLSearchParams(window.location.search);

  // Kiểm tra nếu tham số logout=true
  if (urlParams.get('logout') === 'true') {
    alert('Đăng xuất thành công');
  }
});
