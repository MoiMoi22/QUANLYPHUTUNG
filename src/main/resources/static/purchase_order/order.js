// Mảng chứa danh sách các phụ tùng được thêm
let addedParts = [];

// Hàm tính toán và cập nhật các thông số tóm tắt
function updateSummary() {
    let totalQuantity = 0;
    let totalValue = 0;
    
    // Duyệt qua tất cả các phụ tùng đã thêm
    addedParts.forEach(part => {
        const quantity = part.quantity;

        // Tính tổng số lượng
        totalQuantity += quantity;

        // Tính tổng giá trị trước giảm giá
        totalValue += part.price * quantity;
    });
    
    
    // Cập nhật các thông số vào giao diện
    document.getElementById('product-count').textContent = addedParts.length;
    document.getElementById('total-quantity').textContent = totalQuantity;

    // Cập nhật tổng giá trị và tổng giảm giá
    document.getElementById('total-value').textContent = `${totalValue.toLocaleString()} đ`;
}

// Gọi hàm updateSummary mỗi khi có thay đổi trong bảng phụ tùng
document.addEventListener('input', function (event) {
    if (event.target.matches('.quantity-input') || event.target.matches('.discount-input')) {
        updateSummary();
    }
});


// Gọi hàm cập nhật sau mỗi lần thay đổi thông tin trong bảng
function attachInputEvents() {
    const rows = document.querySelectorAll("table tbody tr");
    if (rows.length === 0) return; // Kiểm tra nếu không có hàng thì thoát sớm

    rows.forEach((row, index) => {
        const quantityInput = row.querySelector('input.quantity-input');
        const priceInput = row.querySelector('input.price-input');

        if (quantityInput) {
            quantityInput.addEventListener("input", () => {
                updateTotal(index); // Cập nhật giá trị thành tiền
                updateSummary(); // Cập nhật thông số tóm tắt
            });
        }
        

        if (priceInput) {
            priceInput.addEventListener("input", () => {
				const price = parseFloat(priceInput.value) || 0;
	            addedParts[index].price = price; // Cập nhật giá trị price vào addedParts
	            updateTotal(index); // Cập nhật lại thành tiền            
                updateSummary(); // Cập nhật thông số tóm tắt
            });
        }
    });
}

// Hàm cập nhật thành tiền cho từng hàng
function updateTotal(rowIndex) {
    const row = document.querySelectorAll("table tbody tr")[rowIndex];
    const quantityInput = row.querySelector('input.quantity-input');
    const priceInput = row.querySelector('input.price-input');

    const quantity = parseInt(quantityInput.value) || 1;
    const price = parseInt(priceInput.value) || 0;

    // Tính toán Thành tiền
    const total = price * quantity;

    // Cập nhật DOM
    const totalCell = row.querySelector("td.total-cell");
    totalCell.textContent = `${total.toLocaleString()} đ`;

    // Cập nhật lại trong dữ liệu
    addedParts[rowIndex].quantity = quantity;
    addedParts[rowIndex].price = price;
    updateSummary(); // Cập nhật thông số tóm tắt
}

// Hàm render lại bảng "Danh sách Phụ tùng"
function renderPartsTable() {
	updateSummary();
    let tableBody = document.querySelector("table tbody");
    tableBody.innerHTML = ""; // Xóa nội dung cũ

    if (addedParts.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" class="text-center">Không có phụ tùng nào được thêm.</td></tr>';
        return;
    }

    addedParts.forEach((part, index) => {
        let row = `
            <tr>
                <td>${index + 1}</td>
                <td>${part.productId}</td>
                <td class="text-left">${part.productName}</td>
                <td>
                    <input type="number" min="1" value= ${part.price} class="form-control text-center price-input" step="1" style="width: 65px; margin: auto;">
                </td>
                </td>
                <td>
                    <input type="number" class="form-control text-center quantity-input"
                        value="${part.quantity}" min="1" style="width: 60px; margin: auto;"
                        data-slt="${part.maxQuantity}"> <!-- Thêm data-slt -->
                </td>
                <td class="text-right total-cell">${(part.price * part.quantity).toLocaleString()} đ</td>
                <td class="text-center">
                    <button class="btn btn-danger btn-sm remove-btn" data-id="${part.productId}" title="Xóa">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });

    // Gắn sự kiện xóa cho các nút "Xóa"
    document.querySelectorAll(".remove-btn").forEach((btn) => {
        btn.addEventListener("click", (event) => {
            const productId = event.target.closest("button").dataset.id;
            removePart(productId);
        });
    });

    // Gắn sự kiện cho ô nhập liệu
    attachInputEvents();
}


// Hàm thêm phụ tùng vào danh sách
function addPart(part) {
    const existingPart = addedParts.find((p) => p.productId === part.productId);
    if (existingPart) {
        alert("Phụ tùng đã tồn tại trong danh sách!");
        return;
    }

    addedParts.push({
        ...part,
    });

    renderPartsTable(); // Cập nhật bảng
}

// Hàm xóa phụ tùng khỏi danh sách
function removePart(productId) {
    addedParts = addedParts.filter((part) => part.productId !== productId);
    renderPartsTable(); // Cập nhật bảng
}

// Gắn sự kiện cho các nút "Thêm" trong danh sách tìm kiếm
document.addEventListener("click", (event) => {
    if (event.target.closest(".add-btn")) {
        const button = event.target.closest(".add-btn");
        const part = {
            productId: button.dataset.id,
            productName: button.dataset.name,
            price: 0,
            quantity: 1,       
        };
        addPart(part);
    }
});

// Lắng nghe sự kiện nhập liệu tìm kiếm
const inputTimPhuTung = document.getElementById('tim_phu_tung');
const renderArea = document.getElementById('render-area');

inputTimPhuTung.addEventListener('input', function () {
    const query = inputTimPhuTung.value.trim();

    if (query) {
        fetch('/order/searchProduct', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ partName: query }),
        })
            .then(response => response.json())
            .then(data => renderTable(data))
            .catch(error => console.error('Error fetching parts:', error));
    } else {
        renderArea.innerHTML = '';
    }
});

// Hàm render bảng danh sách tìm kiếm
function renderTable(parts) {
    if (!parts || parts.length === 0) {
        renderArea.innerHTML = '<p class="text-center">Không tìm thấy phụ tùng nào.</p>';
        return;
    }

    let tableHTML = `
        <table class="table table-bordered table-hover text-center">
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Mã</th>
                    <th>Tên</th>
                    <th>Đơn giá</th>
                    <th>Số lượng tồn</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
    `;

    parts.forEach((part, index) => {
        tableHTML += `
            <tr>
                <td>${index + 1}</td>
                <td>${part.productId}</td>
                <td>${part.productName}</td>
                <td>${part.price.toLocaleString()} đ</td>
                <td>${part.quantity}</td>
                <td>
                    <button class="btn btn-primary add-btn" data-id="${part.productId}" data-name="${part.productName}" data-price="${part.price}" data-slt="${part.quantity}">
                        <i class="fa fa-plus"></i> Thêm
                    </button>
                </td>
            </tr>
        `;
    });

    tableHTML += `
            </tbody>
        </table>
    `;
    renderArea.innerHTML = tableHTML;
}

let shopperId = -1
//Xử lý nhà cung cấp
	//TÌm khách hàng qua sđt
document.getElementById('find-customer').addEventListener('click', function () {
    // Lấy giá trị từ input Số điện thoại
    const phoneNumber = document.getElementById('supplier-phone').value.trim();

    if (!phoneNumber) {
        alert('Vui lòng nhập số điện thoại!');
        return;
    }

    // Gửi yêu cầu đến backend (Spring Boot)
    fetch('/purchase/findSupplier', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ phone: phoneNumber }),
    })
        .then(response => response.json()) // Chuyển đổi phản hồi sang JSON
        .then(data => {
            if (data.success) {
                // Cập nhật tên khách hàng vào input
                document.getElementById('contact-name').value = data.supplier.contactName || 'Không tìm thấy nhà cung cấp';
                document.getElementById('company-name').value = data.supplier.companyName || 'Không tìm thấy nhà cung cấp';
                supplierId = data.supplier.supplierId;
            } else {
                alert(data.message || 'Không tìm thấy khách hàng!');
                document.getElementById('customer-name').value = '';
            }
        })
        .catch(error => {
            console.error('Lỗi khi tìm khách hàng:', error);
            alert('Có lỗi xảy ra. Vui lòng thử lại!');
        });
});

//GỬI DỮ LIỆU VỀ CONTROLLER
function collectInvoiceData() {
    // Lấy thông tin khách hàng
    const supplierPhone = document.getElementById('supplier-phone').value.trim();
    const supplierName = document.getElementById('contact-name').value.trim();
    const total_value = document.getElementById('total-value');

    // Kiểm tra null các thông tin cơ bản
    if (!supplierPhone || !supplierName) {
        alert("Vui lòng nhập đầy đủ thông tin nhà cung cấp!");
        return null; // Ngăn không gửi dữ liệu
    }

    // Kiểm tra danh sách phụ tùng
    if (addedParts.length === 0) {
        alert("Vui lòng thêm ít nhất một phụ tùng vào hóa đơn!");
        return null; // Ngăn không gửi dữ liệu
    }

    const invoiceParts = addedParts.map(part => ({
        productId: part.productId,
        quantity: part.quantity,
        price: part.price
    }));

    // Gộp tất cả thông tin
    return {
        supplierId: supplierId,
        parts: invoiceParts,
        totalCost: parseInt(total_value.innerText.replace(/[,đ]/g, '').trim(), 10),
    };
}



document.getElementById('create-invoice-btn').addEventListener('click', function () {
	const invoiceData = collectInvoiceData();
	console.log(invoiceData);
	
    
    if (!invoiceData) return; // Ngừng nếu dữ liệu không hợp lệ
	console.log(invoiceData);
    fetch('/purchase/createInvoice', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(invoiceData),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Hóa đơn đã được tạo thành công!');
            writeUserData();
        } else {
            alert(data.message || 'Đã xảy ra lỗi khi tạo hóa đơn!');
        }
    })
    .catch(error => {
        console.error('Lỗi khi tạo hóa đơn:', error);
        alert('Có lỗi xảy ra. Vui lòng thử lại!');
    });
});

