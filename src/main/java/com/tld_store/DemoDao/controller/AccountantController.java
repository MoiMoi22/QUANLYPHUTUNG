package com.tld_store.DemoDao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dto.Address;
import dto.Employee;
import dto.Item_Order;
import dto.Order;
import dto.Purchase_Order;
import dto.Shopper;
import dto.Transaction;
import exception.CustomException;

import org.springframework.web.bind.annotation.RequestParam;

import com.tld_store.DemoDao.service.AddressService;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.OrderService;
import com.tld_store.DemoDao.service.PurchaseService;
import com.tld_store.DemoDao.service.ShopperService;
import com.tld_store.DemoDao.service.TransactionService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/accountant")
public class AccountantController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private TransactionService tranService;
	@GetMapping
	public String getAccountantView(ModelMap model) {
		try {
	    	AtomicBoolean isFinalOrder = new AtomicBoolean();

	    	ArrayList<Order> orders = orderService.get10OrderByStatus("PENDING", 1, isFinalOrder);

			model.addAttribute("orders", orders);
			model.addAttribute("isFinalOrder", isFinalOrder);
				    			
		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		return "dashboard/thanh_toan_hoa_don";
	}
	@PostMapping("/process-payment")
	public String proccessPayment(@RequestParam("amount_paid") float amount_paid, @RequestParam("remaining_amount") String remainingDebt, @RequestParam("order_Id") int orderId, ModelMap model) {
		Transaction tran = new Transaction();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    try {
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
		    else {
		    	throw new CustomException("Không có quyền thanh toán");
		    }
		    Employee emp = employeeService.findEmpByUsername(username);
		   tranService.handleRequest(amount_paid, orderId, emp.getId(), "THU");
	
			model.addAttribute("message", "Thanh toán thành công");
		} 
	    catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
			
		}
		model.addAttribute("orders", orderService.getAllOrderByStatus("PENDING"));
		return "dashboard/thanh_toan_hoa_don";
	}
		    
    @GetMapping("/orders_payment/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10OrdersPayment(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
	    	ArrayList<Order> orders = orderService.get10OrderByStatus("PENDING", pageNumber, isFinal);
            response.put("orders", orders);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
    
    @GetMapping("/orders_payment/search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopOrdersPayment(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Order> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = orderService.get10OrderByShoperNameWithStatus(search_name, "PENDING", pageNum, isFinal);
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("orders", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("orders", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
    
}
