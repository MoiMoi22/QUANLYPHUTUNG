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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tld_store.DemoDao.service.AddressService;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.OrderService;
import com.tld_store.DemoDao.service.ProductService;
import com.tld_store.DemoDao.service.PurchaseService;
import com.tld_store.DemoDao.service.ShopperService;
import com.tld_store.DemoDao.service.TransactionService;

import dto.Address;
import dto.Employee;
import dto.Item_Order;
import dto.Order;
import dto.Product;
import dto.Purchase_Order;
import dto.Shopper;
import dto.Transaction;
import exception.CustomException;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	AddressService addressService;
	@Autowired
	ProductService pdService;
	@Autowired
	ShopperService shopperService;
	@Autowired
	OrderService orderService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	EmployeeService employeeService;

    @PostMapping("/searchProduct")
    public ResponseEntity<ArrayList<Product>> searchParts(@RequestBody Map<String, String> request) {
        String partName = request.get("partName");

        // Tìm kiếm danh sách phụ tùng từ cơ sở dữ liệu
        ArrayList<Product> parts = pdService.getProductsByName(partName);
        return ResponseEntity.ok(parts);
    }
    @PostMapping("/findShopper")
    public ResponseEntity<Map<String, Object>> findShopper(@RequestBody Map<String, String> request) {
        String phone_num = request.get("phone");
        Map<String, Object> response = new HashMap<>();
        Shopper sp = new Shopper();
        ArrayList<Address> arr = new ArrayList<>();
        try {
			sp = shopperService.findShopperByPhoneNum(phone_num);
			if(sp == null)
			{
				throw new CustomException("Số điện thoại không hợp lệ");
			}
			arr = addressService.getAddressByShopperId(sp.getId());
            response.put("success", true);
            response.put("message", "Shopper is found successfully");
            response.put("shopper", sp);
            response.put("addresses", arr);

            return ResponseEntity.ok(response);
		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

    }
    
    @GetMapping("/order_maker")
    public String getInvoice() {
        return "info_detail/order_maker";
    }
    
    
    @PostMapping("/add_order")
    public ResponseEntity<?> createInvoice(@RequestBody Map<String, Object> request) {
    	Map<String, Object> response = new HashMap<>();
    	try {
			orderService.handleRequestOrder(request);
			response.put("success", true);
			response.put("message", "Tạo hóa đơn thành công");
			return ResponseEntity.ok(response);
		}
    	catch (CustomException e)
    	{
            response.put("status", "error");
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);		
		}
//        // Kiểm tra null hoặc dữ liệu không hợp lệ
//        if (invoiceDTO.getCustomer() == null || 
//            invoiceDTO.getCustomer().getName() == null || 
//            invoiceDTO.getCustomer().getPhone() == null || 
//            invoiceDTO.getParts() == null || invoiceDTO.getParts().isEmpty()) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Dữ liệu không hợp lệ!"));
//        }
//
//        // Kiểm tra các trường hợp khác
//        if (invoiceDTO.getDiscount() < 0) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Chiết khấu không hợp lệ!"));
//        }
//
//        try {
//            // Xử lý tạo hóa đơn
//            System.out.println("Invoice Data: " + invoiceDTO);
//            return ResponseEntity.ok(Collections.singletonMap("success", true));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("message", "Lỗi khi tạo hóa đơn!"));
//        }
    	
    }
    
    @GetMapping("/view")
    public String view_order(@RequestParam("orderId") int orderId, ModelMap model) {
    	try {
	    	Order order = orderService.getOrderByOrderId(orderId);
	    	ArrayList<Item_Order> items = orderService.getAllItemByOrderId(orderId);
	    	Shopper shopper = shopperService.findShopperById(order.getPeopleId());
	    	
	        model.addAttribute("order", order);
	        model.addAttribute("items", items);
	        model.addAttribute("remainingDebt", (int) order.getRemainingDebt());
	        model.addAttribute("phone", shopper.getPhoneNum());
	        model.addAttribute("name", shopper.getLastName() + " "+ shopper.getFirstName());
	        model.addAttribute("fullAddress", addressService.getFullPathAddress(addressService.getAddressByAddressId(order.getShippingAddressId())));
	        model.addAttribute("mode", "view");
    	} catch (CustomException e) {
    		System.out.println(e.getMessage());
    		model.addAttribute("message", e.getMessage());
    	}
        return "info_detail/order_review";  // Trả về tên view
    }
	
	@GetMapping("all/{pageNum}")
    public ResponseEntity<Map<String, Object>> get10Ordera(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Order> orders = orderService.getTop10Orders(pageNumber, isFinal);
            response.put("users", orders);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
	
	@GetMapping("search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopOrder(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Order> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = orderService.get10OrderByShoperName(search_name,pageNum, isFinal);
        	
        	
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("users", arr);
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
	@GetMapping("/pay_debt")
	public String show_payDebt(@RequestParam("orderId") int orderId, ModelMap model) {
		try {
	    	Order order = orderService.getOrderByOrderId(orderId);
	    	ArrayList<Item_Order> items = orderService.getAllItemByOrderId(orderId);
	    	Shopper shopper = shopperService.findShopperById(order.getPeopleId());
	    	
	        model.addAttribute("order", order);
	        model.addAttribute("items", items);
	        model.addAttribute("remainingDebt", (int) order.getRemainingDebt());
	        model.addAttribute("phone", shopper.getPhoneNum());
	        model.addAttribute("name", shopper.getLastName() + " "+ shopper.getFirstName());
	        model.addAttribute("fullAddress", addressService.getFullPathAddress(addressService.getAddressByAddressId(order.getShippingAddressId())));
	        model.addAttribute("mode", "debt");
    	} catch (CustomException e) {
    		System.out.println(e.getMessage());
    		model.addAttribute("message", e.getMessage());
    	}
        return "info_detail/order_review";  // Trả về tên view
	}
	
	@PostMapping("/pay_debt")
	public String payDebt(@ModelAttribute("orderId") int orderId, @ModelAttribute("money") float money, RedirectAttributes map) {
		
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = "";
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
			transactionService.handleRequest(money, orderId, employeeService.findEmployeeByEmail(username).getId(), "THU");
			map.addFlashAttribute("message", "Success");
			return "redirect:/order";
		}catch (CustomException e) {
			System.out.println(e.getMessage());
			map.addFlashAttribute("message", e.getMessage());
			return "redirect:/order/view?orderId=" + orderId;
		}
		
	}
  
}
