package com.tld_store.DemoDao;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tld_store.DemoDao.imp.ShopperDAOImp;
import com.tld_store.DemoDao.imp.TransactionDAOImp;

import dto.Shopper;
import dto.Transaction;
import dao.TransactionDAO;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(("/addShopper"))
public class HelloController {
    @Autowired
    private Cloudinary cloudinary;
//	@GetMapping("/home")
//	public String hello() {
//		System.out.println("huudung");
//		return "index/index";
//	}
@GetMapping("/getById")
public Transaction getById() throws SQLException {
	TransactionDAO itemOrderDao = new TransactionDAOImp();
    return itemOrderDao.findById(1);
}

@GetMapping("/getAll")
public ArrayList<Transaction> getAllEmployee() throws SQLException {
	TransactionDAO itemOrderDao = new TransactionDAOImp();
    return (ArrayList<Transaction>) itemOrderDao.getAll();
}
//@PostMapping
//public Shopper addShopper(
//		@ModelAttribute Shopper user,
//    @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
//
//    // Upload ảnh lên Cloudinary
//	Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
//            ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Shoppers"));
//    String imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
//
//    user.setLastName(imageUrl);
//    
//    ShopperDAOImp dao = new ShopperDAOImp();
//    
//    dao.insert(user);
//
//    return user;
//}
@GetMapping("/delete")
public int deleteOrder() throws SQLException {
	TransactionDAO itemOrderDao = new TransactionDAOImp();
    return itemOrderDao.delete(new Transaction(
            3,                              // transactionId
            Date.valueOf("2024-11-18"),     // transactionDate
            "Repayment",                    // transactionType
            20.00f,                         // amount
            "Debt Repayment",               // title
            "Repaying order-related debt",  // description
            1,                            // accountantId
            1                            // relatedOrderId
        ));
}

@GetMapping("/update")
public int updateOrder() throws SQLException {
	TransactionDAO orderDao = new TransactionDAOImp();
    return orderDao.update(new Transaction(
            3,                              // transactionId
            Date.valueOf("2024-11-18"),     // transactionDate
            "Repayment",                    // transactionType
            20.00f,                         // amount
            "Debt Repayment turn 1",               // title
            "Repaying order-related debt",  // description
            1,                            // accountantId
            1                            // relatedOrderId
        ));
}

@GetMapping("/getAllById")
public ArrayList<Transaction> getTransactionByAccountantId() throws SQLException {
	TransactionDAO orderDao = new TransactionDAOImp();
    return (ArrayList<Transaction>) orderDao.get10(1);
}

@GetMapping("/getAllRelatedById")
public ArrayList<Transaction> getTransactionByRelatedOrderId() throws SQLException {
	TransactionDAO orderDao = new TransactionDAOImp();
    return (ArrayList<Transaction>) orderDao.getAllTransactionByRelatedOrderIdAndType(101);
}

}
