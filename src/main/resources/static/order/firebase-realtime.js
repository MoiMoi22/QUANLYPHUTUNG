// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.1/firebase-app.js";
import { getDatabase, ref, set, onValue } from "https://www.gstatic.com/firebasejs/9.6.1/firebase-database.js";

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyBVTlPgrLiGZCEC6uUuvOfnYi7LRz6xUbk",
    authDomain: "testrealtime-16ae0.firebaseapp.com",
    databaseURL: "https://testrealtime-16ae0-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "testrealtime-16ae0",
    storageBucket: "testrealtime-16ae0.appspot.com",
    messagingSenderId: "696460364143",
    appId: "1:696460364143:web:f62c3af0cf346b974b009a",
    measurementId: "G-PNGBTLL60D"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

// Function to write data to Firebase Realtime Database
window.writeUserData = function(status) {
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD
    const timestamp = Date.now(); // Dùng làm stt

    set(ref(database, `Notification/${formattedDate}/${timestamp}`), {
        status: status,
        timestamp: timestamp // Lưu timestamp để dễ theo dõi
    }).then(() => {
        //alert("Data written successfully!");
    }).catch((error) => {
        console.error("Error writing data:", error);
    });

};

// Function to get all data for a specific date
let flag = 0
function getUserData() {
	const today = new Date();
    const date = today.toISOString().split('T')[0]; // YYYY-MM-DD
    const dataRef = ref(database, `Notification/${date}`);
    
    onValue(dataRef, (snapshot) => {
        const data = snapshot.val();
        if (data) {
			let latestData = null;
            let latestTimestamp = -Infinity;
            // Duyệt qua các timestamp để lấy thông tin chi tiết
            for (let timestamp in data) {
				const currentData = data[timestamp];
                if (currentData.timestamp > latestTimestamp) {
                    latestTimestamp = currentData.timestamp;
                    latestData = currentData;
                }
            }
            
            if (flag === 0){
				flag = 1;
				return;
			}
            
            if(latestData.status === 'create')
				showKeToanToast('1 hóa đơn đã được tạo, tiến hành kiểm tra và thực hiện thanh toán', 'success');
			else
				showKhoToast('1 hóa đơn đã được thanh toán, tiến hành kiểm tra và thực hiện xuất nhập', 'success');
        } else {
            console.log("Không có dữ liệu cho ngày này!");
        }
    }, (error) => {
        console.error("Lỗi khi lấy dữ liệu:", error);
    });
}

getUserData();


