package com.tld_store.DemoDao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tld_store.DemoDao.service.ShopperService;

import dto.Shopper;
import exception.CustomException;

@Controller
@RequestMapping("/shopper")
public class ShopperController {
	@Autowired
	private ShopperService empService;
	
	@GetMapping("/add_shopper")
    public String add_shopper( Model model) {
    	Shopper shopper = new Shopper();
    	shopper.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
        model.addAttribute("shopper", shopper);
        model.addAttribute("mode", "add");
        return "info_detail/shopper_profile";  // Trả về tên view
    }
	
	@PostMapping("/add_shopper")
    public String add_shopper( @ModelAttribute Shopper shopper, @RequestParam("profileImage") MultipartFile img, ModelMap model) {		
		try {
			empService.insertShopper(img, shopper);
			model.addAttribute("message", "Thêm khách hàng thành công");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			shopper = new Shopper();
	    	shopper.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
	        model.addAttribute("shopper", shopper);
	        model.addAttribute("mode", "add");
		}
		
        return "info_detail/shopper_profile";  // Trả về tên view
    }
	
    @GetMapping("/view")
    public String view_shopper(@RequestParam("customerId") int customerId, Model model) {
    	Shopper shopper = empService.findShopperById(customerId);
        model.addAttribute("shopper", shopper);
        model.addAttribute("mode", "view");
        return "info_detail/shopper_profile";  // Trả về tên view
    }
    
    @GetMapping("/edit")
    public String edit_shopper(@RequestParam("customerId") int customerId, Model model) {
    	Shopper shopper = empService.findShopperById(customerId);
        model.addAttribute("shopper", shopper);
        model.addAttribute("mode", "edit");
        return "info_detail/shopper_profile";  // Trả về tên view
    }
    
	@PostMapping("/edit")
    public String edit_shopper( @ModelAttribute Shopper shopper, @RequestParam("profileImage") MultipartFile img, ModelMap model, @RequestParam("customerId") int customerId) {		
		try {
			shopper.setId(customerId);
			empService.updateShopper(img, shopper);
			model.addAttribute("message", "Cập nhập khách hàng thành công");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			shopper = empService.findShopperById(customerId);
	    	model.addAttribute("shopper", shopper);
	        model.addAttribute("mode", "edit");
		}
		
        return "info_detail/shopper_profile";  // Trả về tên view
    }
	
    @GetMapping("/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10Shoppers(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Shopper> employees = empService.getTop10Shoppers(pageNumber, isFinal);
            response.put("users", employees);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }	
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteShopper(@PathVariable String id) {
    	Shopper emp = new Shopper();
    	emp.setId(Integer.parseInt(id));
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	empService.deleteShopper(emp);
            response.put("status", "success");
            response.put("message", "Shopper deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("id", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
    
    @GetMapping("/search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopShoppers(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Shopper> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = empService.getShoppersByFullName(search_name,pageNum, isFinal);
        	
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("users", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
        	System.out.println("123456789");
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("users", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
    
}
