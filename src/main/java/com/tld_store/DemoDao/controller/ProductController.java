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

import com.tld_store.DemoDao.service.CategoryService;
import com.tld_store.DemoDao.service.ProductService;

import dto.Product;
import dto.ProductCategory;
import exception.CustomException;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/add_product")
    public String add_product(Model model) {
    	Product product = new Product();
    	product.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
    	
    	ArrayList<ProductCategory> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
    	
        model.addAttribute("mode", "add");
        return "info_detail/product_detail";  // Trả về tên view
    }
	
	@PostMapping("/add_product")
    public String add_product( @ModelAttribute Product product, @RequestParam("profileImage") MultipartFile img, ModelMap model) {		
		try {
			productService.insertProduct(img, product);
			model.addAttribute("message", "Add product successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			product = new Product();
	        model.addAttribute("product", product);

	    	ArrayList<ProductCategory> categories = categoryService.getAllCategories();
	        model.addAttribute("categories", categories);
	        
	        model.addAttribute("mode", "add");       
		}
		
        return "info_detail/product_detail";  // Trả về tên view
    }
    
    @GetMapping("/view")
    public String view_product(@RequestParam("productId") String productId, Model model) {
    	Product product = productService.findProductById(productId);
    	
        model.addAttribute("product", product);
 
    	ArrayList<ProductCategory> categories = new ArrayList<>();        
        ProductCategory category = categoryService.findCategoryById(product.getCategoryId());
        categories.add(category);
        
        model.addAttribute("categories", categories );
        
        model.addAttribute("mode", "view");
        
        return "info_detail/product_detail";  // Trả về tên view
    }
    
    @GetMapping("/edit")
    public String edit_product(@RequestParam("productId") String productId, Model model) {
    	Product product = productService.findProductById(productId);
    	ArrayList<ProductCategory> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("mode", "edit");
        return "info_detail/product_detail";  // Trả về tên view
    }	
	@PostMapping("/edit")
    public String edit_category( @ModelAttribute Product product, @RequestParam("profileImage") MultipartFile img, ModelMap model, @RequestParam("productId") String productId) {		
		try {
//			product.setProductId(productId);
			productService.updateProduct(img, product);
			model.addAttribute("message", "Update product successfully!");

		}
		catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			product = productService.findProductById(product.getProductId());
	    	ArrayList<ProductCategory> categories = categoryService.getAllCategories();
	        model.addAttribute("product", product);
	        model.addAttribute("categories", categories);
	        model.addAttribute("mode", "edit");
		}
		
        return "info_detail/product_detail";  // Trả về tên view
    }
    @GetMapping("/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10Products(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Product> products = productService.getTop10Products(pageNumber, isFinal);
            response.put("users", products);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }	
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable String id) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	Product product = new Product();
    	product.setProductId(id);
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	productService.deleteProduct(product);
            response.put("status", "success");
            response.put("message", "Product deleted successfully");
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
    public ResponseEntity<Map<String, Object>> findTopEmployee(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Product> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = productService.getTop10ProductsByName(search_name,pageNum, isFinal);
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("users", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("users", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
}
