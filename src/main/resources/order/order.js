// Mảng chứa danh sách các phụ tùng được thêm
let addedParts = [];

// Hàm tính toán và cập nhật các thông số tóm tắt
function updateSummary() {
    let totalQuantity = 0;
    let totalValue = 0;
    let totalDiscount = 0;

    // Duyệt qua tất cả các phụ tùng đã thêm
    addedParts.forEach(part => {
        const quantity = part.quantity;
        const discount = part.discount;

        // Tính tổng số lượng
        totalQuantity += quantity;

        // Tính tổng giá trị trước giảm giá
        totalValue += part.price * quantity;

        // Tính tổng giảm giá
        totalDiscount += (part.price * quantity * (discount / 100));
    });
    
     // Cập nhật chiết khấu hóa đơn
    const invoiceDiscount = parseInt(document.getElementById('invoice-discount').value) || 0;

    // Tính toán tổng giá trị sau chiết khấu hóa đơn
    const finalDiscount = totalDiscount + (totalValue * invoiceDiscount / 100);
    
    if (finalDiscount < 0 || finalDiscount > totalValue) {
        alert("Chiết khấu từ 0% đến 100% của tổng giá trị.");
        return; // Ngừng cập nhật
    }
    
    const finalValue = totalValue - finalDiscount;
    
    // Cập nhật các thông số vào giao diện
    document.getElementById('product-count').textContent = addedParts.length;
    document.getElementById('total-quantity').textContent = totalQuantity;
    document.getElementById('total-discount').textContent = `${finalDiscount.toLocaleString()} đ`;

    // Cập nhật tổng giá trị và tổng giảm giá
    document.getElementById('total-value').textContent = `${finalValue.toLocaleString()} đ`;
}

// Gắn sự kiện thay đổi chiết khấu hóa đơn
document.getElementById('invoice-discount').addEventListener('input', updateSummary);

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
        const discountInput = row.querySelector('input.discount-input');

        if (quantityInput) {
            quantityInput.addEventListener("input", () => {
                const maxQuantity = parseInt(quantityInput.dataset.slt); // Lấy số lượng tồn từ dataset
				if (parseInt(quantityInput.value) > maxQuantity) {
                    quantityInput.value = maxQuantity; // Thiết lập lại giá trị nếu vượt quá
                    alert(`Số lượng không được vượt quá ${maxQuantity}`);
                }else if (parseInt(quantityInput.value) <= 0) {
					// Thiết lập lại giá trị nếu vượt quá
					quantityInput.value = 1;
                    alert(`Số lượng không được nhỏ hơn 1`);
                }
                updateTotal(index); // Cập nhật giá trị thành tiền
                updateSummary(); // Cập nhật thông số tóm tắt
                
            });
            discountInput.addEventListener("input", () => {
                if (parseInt(discountInput.value) > 100) {
                    discountInput.value = 100; // Thiết lập lại giá trị nếu vượt quá
                    alert("Chiết khấu không được lớn hơn 100%");
                }else if (parseInt(discountInput.value) < 0) {
					discountInput.value = 0;
                    alert("Chiết khấu không được nhỏ hơn 0%");
                }
            });
        
        }
        

        if (discountInput) {
            discountInput.addEventListener("input", () => {
                updateTotal(index); // Cập nhật giá trị thành tiền
                updateSummary(); // Cập nhật thông số tóm tắt
            });
        }
    });
}

// Hàm cập nhật thành tiền cho từng hàng
function updateTotal(rowIndex) {
    const row = document.querySelectorAll("table tbody tr")[rowIndex];
    const quantityInput = row.querySelector('input.quantity-input');
    const discountInput = row.querySelector('input.discount-input');

    const quantity = parseInt(quantityInput.value) || 1;
    const discount = parseInt(discountInput.value) || 0;
    const price = addedParts[rowIndex].price;

    // Tính toán Thành tiền
    const total = price * quantity * (1 - discount / 100);

    // Cập nhật DOM
    const totalCell = row.querySelector("td.total-cell");
    totalCell.textContent = `${total.toLocaleString()} đ`;

    // Cập nhật lại trong dữ liệu
    addedParts[rowIndex].quantity = quantity;
    addedParts[rowIndex].discount = discount;
    updateSummary(); // Cập nhật thông số tóm tắt
}

// Hàm render lại bảng "Danh sách Phụ tùng"
function renderPartsTable() {
	updateSummary();
    let tableBody = document.querySelector("table tbody");
    tableBody.innerHTML = ""; // Xóa nội dung cũ

    if (addedParts.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="8" class="text-center">Không có phụ tùng nào được thêm.</td></tr>';
        return;
    }

    addedParts.forEach((part, index) => {
        let row = `
            <tr>
                <td>${index + 1}</td>
                <td>${part.productId}</td>
                <td class="text-left">${part.productName}</td>
                <td class="text-right">${part.price.toLocaleString()} đ</td>
                <td>
                    <input type="number" class="form-control text-center quantity-input"
                        value="${part.quantity}" min="1" style="width: 60px; margin: auto;"
                        data-slt="${part.maxQuantity}"> <!-- Thêm data-slt -->
                </td>
                <td>
                    <input type="number" class="form-control text-center discount-input"
                        value="${part.discount}" min="0" max="100" step="1" style="width: 65px; margin: auto;">
                </td>
                <td class="text-right total-cell">${(part.price * part.quantity * (1 - part.discount / 100)).toLocaleString()} đ</td>
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
        maxQuantity: part.maxQuantity, // Thêm maxQuantity vào đối tượng
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
            price: parseInt(button.dataset.price),
            quantity: 1,
            discount: 0,
            maxQuantity: parseInt(button.dataset.slt),
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
//Xử lý khách hàng
	//TÌm khách hàng qua sđt
document.getElementById('find-customer').addEventListener('click', function () {
    // Lấy giá trị từ input Số điện thoại
    const phoneNumber = document.getElementById('customer-phone').value.trim();

    if (!phoneNumber) {
        alert('Vui lòng nhập số điện thoại!');
        return;
    }

    // Gửi yêu cầu đến backend (Spring Boot)
    fetch('/order/findShopper', {
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
                document.getElementById('customer-name').value = data.shopper.lastName + ' ' + data.shopper.firstName || 'Không tìm thấy khách hàng';
                shopperId = data.shopper.id;
                const addrSelect = document.getElementById('addr');

	            // Xóa các tùy chọn hiện có (ngoại trừ mặc định)
	            while (addrSelect.options.length > 1) {
	                addrSelect.remove(1);
	            }
	
	            // Thêm danh sách địa chỉ vào <select>
	            data.addresses.forEach((address, index) => {
	                const option = document.createElement('option');
	                option.value = address.addressId; // ID giả lập hoặc giá trị duy nhất
	                option.textContent = address.addressName;
	                addrSelect.appendChild(option);
	            });
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
document.addEventListener("DOMContentLoaded", function () {
    const addrSelect = document.getElementById('addr');
    const provinceSelect = document.getElementById('province');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');
    const streetInput = document.getElementById('street');

    // Hàm bật/tắt các trường
    function toggleFields(isEditable) {
        provinceSelect.disabled = !isEditable;
        districtSelect.disabled = !isEditable;
        wardSelect.disabled = !isEditable;
        streetInput.disabled = !isEditable;

        if (!isEditable) {
            // Đặt lại giá trị mặc định khi không cho chỉnh sửa
            provinceSelect.value = "";
            districtSelect.value = "";
            wardSelect.value = "";
            streetInput.value = "";
        }
    }

    // Theo dõi sự kiện thay đổi trên select #addr
    addrSelect.addEventListener('change', function () {
        if (addrSelect.value === "0") {
            // Nếu chọn "Địa chỉ mới"
            toggleFields(true);
        } else {
            // Nếu chọn địa chỉ có sẵn
            toggleFields(false);
        }
    });

    // Mặc định disable các trường khi trang tải
    toggleFields(addrSelect.value === "0");
});
//GỬI DỮ LIỆU VỀ CONTROLLER
function collectInvoiceData() {
    // Lấy thông tin khách hàng
    const customerName = document.getElementById('customer-name').value.trim();
    const customerPhone = document.getElementById('customer-phone').value.trim();
    const total_value = document.getElementById('total-value');
    const addrSelect = document.getElementById('addr');
    const selectedAddress = addrSelect.value === "0" ? {
        province: document.getElementById('province').value.trim(),
        district: document.getElementById('district').value.trim(),
        ward: document.getElementById('ward').value.trim(),
        street: document.getElementById('street').value.trim(),
        address: document.getElementById('street').value.trim()+', '+document.getElementById('ward').value.trim(),
    } : addrSelect.options[addrSelect.selectedIndex]?.value;

    // Kiểm tra null các thông tin cơ bản
    if (!customerName || !customerPhone) {
        alert("Vui lòng nhập đầy đủ thông tin khách hàng!");
        return null; // Ngăn không gửi dữ liệu
    }

    if (addrSelect.value === "0" && (!selectedAddress.province || !selectedAddress.district || !selectedAddress.ward || !selectedAddress.street)) {
        alert("Vui lòng nhập đầy đủ thông tin địa chỉ mới!");
        return null; // Ngăn không gửi dữ liệu
    }

    // Lấy chiết khấu hóa đơn
    const invoiceDiscount = parseInt(document.getElementById('invoice-discount').value) || 0;

    // Kiểm tra danh sách phụ tùng
    if (addedParts.length === 0) {
        alert("Vui lòng thêm ít nhất một phụ tùng vào hóa đơn!");
        return null; // Ngăn không gửi dữ liệu
    }

    const invoiceParts = addedParts.map(part => ({
        productId: part.productId,
        quantity: part.quantity,
        discount: part.discount
    }));

    // Gộp tất cả thông tin
    return {
        customer: {
            shopperId: shopperId,
            phone: customerPhone,
            //address: selectedAddress,
            addressId: addrSelect.value === "0" ?  "-1": selectedAddress,
            commune: addrSelect.value === "0" ?  selectedAddress.ward: '',
            street: addrSelect.value === "0" ?  selectedAddress.street: '',
        },
        discount: invoiceDiscount,
        parts: invoiceParts,
        totalCost: parseInt(total_value.innerText.replace(/[,đ]/g, '').trim(), 10),
    };
}

document.getElementById('create-invoice-btn').addEventListener('click', function () {
    const invoiceData = collectInvoiceData();
    if (!invoiceData) return; // Ngừng nếu dữ liệu không hợp lệ
	console.log(invoiceData);
    fetch('/order/createInvoice', {
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
        } else {
            alert(data.message || 'Đã xảy ra lỗi khi tạo hóa đơn!');
        }
    })
    .catch(error => {
        console.error('Lỗi khi tạo hóa đơn:', error);
        alert('Có lỗi xảy ra. Vui lòng thử lại!');
    });
});


//Xử lý tỉnh
// Lấy các phần tử
const provinceSelect = document.getElementById('province');
const districtSelect = document.getElementById('district');
const wardSelect = document.getElementById('ward');

// Hàm làm rỗng select
function clearSelect(selectElement) {
    selectElement.innerHTML = '<option value="" disabled selected>Chọn</option>';
}

// Hàm gọi API và thêm dữ liệu vào select
function populateSelect(selectElement, data, valueKey, textKey) {
    clearSelect(selectElement);
    data.forEach(item => {
	    for (const [id, name] of Object.entries(item)) {
	        const option = document.createElement('option');
	        option.value = id;
	        option.textContent = name;
	        selectElement.appendChild(option);
        }

        
    });
}

// Khi chọn tỉnh, tải huyện
provinceSelect.addEventListener('change', () => {
    clearSelect(districtSelect);
    clearSelect(wardSelect);

    const provinceId = provinceSelect.value;
    if (provinceId) {
        fetch(`/api/districts?provinceId=${provinceId}`)
            .then(response => response.json())
            .then(data => {
                populateSelect(districtSelect, data, 'id', 'name');
            })
            .catch(error => console.error('Error loading districts:', error));
    }
});

// Khi chọn huyện, tải xã
districtSelect.addEventListener('change', () => {
    clearSelect(wardSelect);

    const districtId = districtSelect.value;
    if (districtId) {
        fetch(`/api/wards?districtId=${districtId}`)
            .then(response => response.json())
            .then(data => {
                populateSelect(wardSelect, data, 'id', 'name');
            })
            .catch(error => console.error('Error loading wards:', error));
    }
});

// Hàm khởi tạo, tải danh sách tỉnh
function initProvinces() {
    fetch('/api/provinces')
        .then(response => response.json())
        .then(data => {
            populateSelect(provinceSelect, data, 'id', 'name');
            
        })
        .catch(error => console.error('Error loading provinces:', error));
}

// Khởi tạo dữ liệu ban đầu
initProvinces();