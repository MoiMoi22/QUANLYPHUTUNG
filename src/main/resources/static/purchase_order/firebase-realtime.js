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
window.writeUserData = function() {
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD
    const timestamp = Date.now(); // Dùng làm stt
    const message = "Sample Message"; // Ensure a valid message

    set(ref(database, `Notification/${formattedDate}/${timestamp}`), {
        message: message,
        timestamp: timestamp // Lưu timestamp để dễ theo dõi
    }).then(() => {
        //alert("Data written successfully!");
    }).catch((error) => {
        console.error("Error writing data:", error);
    });
    
    
    //Thông báo
	const toastElement = document.getElementById('toastExample');
	const toast = new bootstrap.Toast(toastElement);
	toast.show();
	console.log('dung');
};

// Function to listen for real-time updates for all users
/*function listenForAllUserUpdates(id) {
    const usersRef = ref(database, 'mess2');
    onValue(usersRef, (snapshot) => {
        const data = snapshot.val();
        document.getElementById(id).innerText = JSON.stringify(data, null, 2);
    });
}*/

// Call this function to start listening for all user updates
//listenForAllUserUpdates();
