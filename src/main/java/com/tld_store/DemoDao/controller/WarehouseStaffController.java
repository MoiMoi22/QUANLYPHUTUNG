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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tld_store.DemoDao.service.AddressService;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.OrderService;
import com.tld_store.DemoDao.service.PurchaseService;
import com.tld_store.DemoDao.service.ShopperService;
import com.tld_store.DemoDao.service.SupplierService;

import dto.Address;
import dto.Employee;
import dto.Item_Order;
import dto.Purchase_Order_Item;
import dto.Order;
import dto.Purchase_Order;
import dto.Shopper;
import dto.Supplier;

import exception.CustomException;

@Controller
@RequestMapping("/warehouseStaff")
public class WarehouseStaffController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ShopperService shopperService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private EmployeeService empService;

	@GetMapping
	public String getViewWareHouse(ModelMap model) {
		try {

	    	ArrayList<Order> orders = orderService.getAllOrdersWithStatuses();
	    	ArrayList<Purchase_Order> purchaseOrders = purchaseService.getAllOrdersWithStatuses();

			model.addAttribute("orders", orders);
			
			model.addAttribute("purchaseOrders", purchaseOrders);
				    			
		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		return "dashboard/nhap_xuat_hang";
	}
	
	@GetMapping("/order_review")
	public String getOrder_review(@RequestParam("orderId") int orderId, ModelMap model) {
		Order order = orderService.getOrderByOrderId(orderId);
		
		Shopper shopper = shopperService.findShopperById(order.getPeopleId());
		
		ArrayList<Item_Order> items = new ArrayList<>();
		
		items = orderService.getAllItemByOrderId(orderId);
		
		
		Address address = addressService.getAddressByAddressId(order.getShippingAddressId());
		
		String fullAddress = addressService.getFullPathAddress(address);
		
		model.addAttribute("order", order);
		
		model.addAttribute("name", shopper.getLastName() +" "+ shopper.getFirstName());
		model.addAttribute("phone", shopper.getPhoneNum());
		
		model.addAttribute("items", items);
		
		model.addAttribute("fullAddress", fullAddress);
		return "info_detail/don_xuat_hang";
	}
	
	@GetMapping("/accept_order")
	public String accept_order(@RequestParam("orderId") int orderId, ModelMap model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    try {
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
		    else {
		    	throw new CustomException("Không có quyền xác nhận");
		    }
		    Employee emp = empService.findEmpByUsername(username);
			orderService.acceptOrder(orderId, emp.getId());
			
			model.addAttribute("message", "Xác nhận đơn hàng thành công!");
		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());

		}
		model.addAttribute("orders", orderService.getAllOrdersWithStatuses());
		model.addAttribute("purchaseOrders", purchaseService.getAllOrdersWithStatuses());
		
		return "dashboard/nhap_xuat_hang";
	}
	
	@GetMapping("/deny_order")
	public String deny_order(@RequestParam("orderId") int orderId, ModelMap model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    try {
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
		    else {
		    	throw new CustomException("Không có quyền xác nhận");
		    }
		    Employee emp = empService.findEmpByUsername(username);
			orderService.denyOrder(orderId, emp.getId());
			
			model.addAttribute("message", "Hủy đơn hàng thành công!");
		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());

		}
		model.addAttribute("orders", orderService.getAllOrdersWithStatuses());
		model.addAttribute("purchaseOrders", purchaseService.getAllOrdersWithStatuses());
		
		return "dashboard/nhap_xuat_hang";
	}

	@GetMapping("order/all")
    public ResponseEntity<Map<String, Object>> getAllOrder() {
    	Map<String, Object> response = new HashMap<>();
        try {
        	ArrayList<Order> orders = orderService.getAllOrdersWithStatuses();
            response.put("orders", orders);
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
	
	@GetMapping("order/search/{input_search}")
    public ResponseEntity<Map<String, Object>> findTopOrder(@PathVariable("input_search") String search_name) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Order> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	arr = orderService.getAllOrdersByShopperNameWithStatuses(search_name);
        	
        	
            response.put("status", "success");
            response.put("orders", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
        	System.out.println(e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("orders", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
	
    @GetMapping("purchase/search/{input_search}")
    public ResponseEntity<Map<String, Object>> findTopPurchaseOrder(@PathVariable("input_search") String search_name) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Purchase_Order> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	arr = purchaseService.getAllOrdersByShopperNameWithStatuses(search_name);
        	
        	
            response.put("status", "success");
            response.put("purchaseOrders", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
        	System.out.println(e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("purchaseOrders", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
    
    @GetMapping("purchase/all")
    public ResponseEntity<Map<String, Object>> get10PurchaseOrder() {
    	Map<String, Object> response = new HashMap<>();
        try {
        	ArrayList<Purchase_Order> orders = purchaseService.getAllOrdersWithStatuses();
            response.put("purchaseOrders", orders);
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
    
	@GetMapping("/purchase_review")
	public String getPurchase_review(@RequestParam("orderId") int orderId, ModelMap model) {
		Purchase_Order order = purchaseService.getPurchaseOrderByOrderId(orderId);
		
		Supplier supplier = supplierService.findSupplierById(order.getPeopleId());
		
		ArrayList<Purchase_Order_Item> items = new ArrayList<>();
		
		items = purchaseService.getAllItemByOrderId(orderId);
				
		Address address = addressService.getAddressByAddressId(supplier.getSupplierAddressId());
		
		String fullAddress = addressService.getFullPathAddress(address);
		
		model.addAttribute("order", order);
		
		model.addAttribute("companyName", supplier.getCompanyName());
		model.addAttribute("contactName", supplier.getContactName());
		
		model.addAttribute("items", items);
		
		model.addAttribute("fullAddress", fullAddress);
		return "info_detail/don_nhap_hang";
	}
	
	@GetMapping("/accept_purchase")
	public String accept_purchase(@RequestParam("orderId") int orderId, ModelMap model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    try {
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
		    else {
		    	throw new CustomException("Không có quyền xác nhận");
		    }
		    Employee emp = empService.findEmpByUsername(username);
			purchaseService.acceptPurchaseOrder(orderId, emp.getId());
			
			model.addAttribute("message", "Xác nhận nhập hàng thành công!");
		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());

		}
		model.addAttribute("orders", orderService.getAllOrdersWithStatuses());
		model.addAttribute("purchaseOrders", purchaseService.getAllOrdersWithStatuses());
		
		return "dashboard/nhap_xuat_hang";
	}
	
	@GetMapping("/deny_purchase")
	public String deny_purchase(@RequestParam("orderId") int orderId, ModelMap model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    try {
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
		    else {
		    	throw new CustomException("Không có quyền xác nhận");
		    }
		    Employee emp = empService.findEmpByUsername(username);
			purchaseService.denyOrder(orderId, emp.getId());
			
			model.addAttribute("message", "Hủy nhập hàng thành công!");
		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());

		}
		model.addAttribute("orders", orderService.getAllOrdersWithStatuses());
		model.addAttribute("purchaseOrders", purchaseService.getAllOrdersWithStatuses());
		
		return "dashboard/nhap_xuat_hang";
	}
	
}
