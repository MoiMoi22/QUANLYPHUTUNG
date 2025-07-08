package com.tld_store.DemoDao.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tld_store.DemoDao.service.CategoryService;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.OrderService;
import com.tld_store.DemoDao.service.ProductService;
import com.tld_store.DemoDao.service.PurchaseService;
import com.tld_store.DemoDao.service.ShopperService;
import com.tld_store.DemoDao.service.SupplierService;
import com.tld_store.DemoDao.service.TransactionService;

import dto.Employee;
import dto.Order;
import dto.Product;
import dto.ProductCategory;
import dto.Purchase_Order;
import dto.Shopper;
import dto.Supplier;
import dto.Transaction;
import exception.CustomException;

@Controller
public class DashBoardController {
	@Autowired
	private TransactionService transService;
	
	@Autowired
	private EmployeeService empService;
	@Autowired
	private ShopperService shopperService;
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private PurchaseService purchaseService;
	
	
	@GetMapping("/dashboard")
	public String index() {
		return "dashboard/index";
	}
	
	@GetMapping("/user")
	public String user(Model model) throws SQLException {
		try {
	    	AtomicBoolean isFinalEmp = new AtomicBoolean();
	    	AtomicBoolean isFinalShopper = new AtomicBoolean();
	    	AtomicBoolean isFinalSupplier = new AtomicBoolean();

	    	ArrayList<Employee> employees = empService.getTop10Employees(1, isFinalEmp);
	    	ArrayList<Shopper> shoppers = shopperService.getTop10Shoppers(1, isFinalShopper);
	    	ArrayList<Supplier> suppliers = supplierService.getTop10Suppliers(1, isFinalSupplier);

			model.addAttribute("employees", employees);
			model.addAttribute("isFinalEmployee", isFinalEmp);
			
			model.addAttribute("customers", shoppers);
			model.addAttribute("isFinalCustomer", isFinalShopper);

			model.addAttribute("suppliers", suppliers);
			model.addAttribute("isFinalSupplier", isFinalSupplier);
				    			
		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		
		return "dashboard/user";
	}
	
	@GetMapping("/product")
	public String product(Model model) {
		
		try {
	    	AtomicBoolean isFinalProduct = new AtomicBoolean();
	    	AtomicBoolean isFinalCategory = new AtomicBoolean();

	    	ArrayList<Product> products = productService.getTop10Products(1, isFinalProduct);
	    	ArrayList<ProductCategory> categories = categoryService.getTop10Categories(1, isFinalCategory);

			model.addAttribute("products", products);
			model.addAttribute("isFinalProduct", isFinalProduct);
			
			model.addAttribute("categories", categories);
			model.addAttribute("isFinalCategory", isFinalCategory);
				    			
		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		
		return "dashboard/product";
	}
	
	@GetMapping("/order")
	public String order(Model model) {
		
		try {
	    	AtomicBoolean isFinalOrder = new AtomicBoolean();
	    	AtomicBoolean isFinalPurchaseOrder = new AtomicBoolean();

	    	ArrayList<Order> orders = orderService.getTop10Orders(1, isFinalOrder);
	    	ArrayList<Purchase_Order> purchaseOrders = purchaseService.getTop10Orders(1, isFinalPurchaseOrder);

			model.addAttribute("orders", orders);
			model.addAttribute("isFinalOrder", isFinalOrder);
			
			model.addAttribute("purchaseOrders", purchaseOrders);
			model.addAttribute("isFinalPurchaseOrder", isFinalPurchaseOrder);
				    			
		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		
		return "dashboard/order";
	}
	
	@GetMapping("/statistic")
	public String statistic() {
		return "dashboard/statistic";
	}
	
	@GetMapping("/transaction")
	public String transaction(Model model) {
		try {
			AtomicBoolean isFinalReceive = new AtomicBoolean();;
			AtomicBoolean isFinalSpend = new AtomicBoolean();;
			
			ArrayList<Transaction> receives = transService.get10TransactionByType("THU", 1, isFinalReceive);
			ArrayList<Transaction> spends = transService.get10TransactionByType("CHI", 1, isFinalSpend);
			
			model.addAttribute("transaction_receives", receives);
			model.addAttribute("transaction_spends", spends);
			model.addAttribute("isFinalReceive", isFinalReceive);
			model.addAttribute("isFinalSpend", isFinalSpend);
		}catch (CustomException e) {
			System.out.println(e.getMessage());
			model.addAttribute("message", e.getMessage());
		}
		return "dashboard/transaction";
	}
	
}
