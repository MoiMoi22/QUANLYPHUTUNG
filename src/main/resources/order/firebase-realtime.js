// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.1/firebase-app.js";
import { getDatabase, ref, set, onValue , query, orderByChild, limitToLast} from "https://www.gstatic.com/firebasejs/9.6.1/firebase-database.js";

const today = new Date();
const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD
	
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
window.writeUserData = function(type, id) {
    const timestamp = Date.now(); // Dùng làm stt
    //const message = "Sample Message1"; // Ensure a valid message

    set(ref(database, `Notification/${formattedDate}/${timestamp}`), {
        type: type,
        id: id
    }).then(() => {
        //alert("Data written successfully!");
    }).catch((error) => {
        console.error("Error writing data:", error);
    });
    
    
    //Thông báo
	const toastElement = document.getElementById('toastExample');
	const toast = new bootstrap.Toast(toastElement);
	toast.show();
};

// Function to listen for real-time updates for all users
function listenForAllUserUpdates() {	
    const usersRef = ref(database, 'Notification/'+`${formattedDate}`);
    // Truy vấn để lấy phần tử mới nhất
    const lastNotificationRef = query(usersRef, orderByChild('timestamp'), limitToLast(1));
    
    onValue(lastNotificationRef, (snapshot) => {
		// Kiểm tra nếu đã lắng nghe trước đó
	    if (check === 0) {		
			check = 1;
	        return;
	    }

        const data = snapshot.val();
        
        // Lấy phần tử mới nhất trong dữ liệu
        const key = Object.keys(data)[0]; // Lấy key của phần tử mới nhất
        const notification = data[key];
        const id = notification.id; // Truy cập vào message của phần tử mới nhất
		
		if (notification.type === 'order')
        	showToast('Có 1 đơn hàng mới được thêm: ' + id, 'success', 'ketoan');
        
        
    });   
}

// Call this function to start listening for all user updates
let check = 0
listenForAllUserUpdates();


