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

import dto.ProductCategory;
import exception.CustomException;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/add_category")
    public String add_category(ModelMap model) {
		ProductCategory category = new ProductCategory();
    	
        model.addAttribute("category", category);
        model.addAttribute("mode", "add");
        return "info_detail/category_detail";  // Trả về tên view
    }
	
	@PostMapping("/add_category")
    public String add_category( @ModelAttribute ProductCategory category, ModelMap model) {		
		try {
			categoryService.insertCategory(category);
			model.addAttribute("message", "Add category successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			category = new ProductCategory();
			
	        model.addAttribute("category", category);
	        model.addAttribute("mode", "add");
	        
		}
		
        return "info_detail/category_detail";  // Trả về tên view
    }
    
    @GetMapping("/view")
    public String view_category(@RequestParam("categoryId") String categoryId, ModelMap model) {
    	ProductCategory category = categoryService.findCategoryById(categoryId);
    	
        model.addAttribute("category", category);
         
        model.addAttribute("mode", "view");
        
        return "info_detail/category_detail";  // Trả về tên view
    }
    
    @GetMapping("/edit")
    public String edit_category(@RequestParam("categoryId") String categoryId, Model model) {
    	ProductCategory category = categoryService.findCategoryById(categoryId);
    	System.out.println(category);
        model.addAttribute("category", category);
        
        model.addAttribute("mode", "edit");
        return "info_detail/category_detail";  // Trả về tên view
    }	
	@PostMapping("/edit")
    public String edit_category( @ModelAttribute ProductCategory category, @RequestParam("profileImage") MultipartFile img, ModelMap model, @RequestParam("categoryId") String categoryId) {		
		try {
			category.setCategoryId(categoryId);
			categoryService.updateCategory(category);
			model.addAttribute("message", "Update category successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			category = categoryService.findCategoryById(categoryId);
	        model.addAttribute("category", category);
	        model.addAttribute("mode", "edit");
		}
		
        return "info_detail/category_detail";  // Trả về tên view
    }
    @GetMapping("/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10Categories(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<ProductCategory> categories = categoryService.getTop10Categories(pageNumber, isFinal);
            response.put("users", categories);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }	
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable String id) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ProductCategory category = new ProductCategory();
    	category.setCategoryId(id);
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	categoryService.deleteCategory(category);
            response.put("status", "success");
            response.put("message", "Category deleted successfully");
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
    public ResponseEntity<Map<String, Object>> findTopCategory(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<ProductCategory> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = categoryService.getTop10CategoriesByName(search_name,pageNum, isFinal);
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
