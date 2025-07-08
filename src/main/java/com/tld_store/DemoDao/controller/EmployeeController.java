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

import com.tld_store.DemoDao.service.EmployeeService;

import dto.Employee;
import exception.CustomException;


@Controller
@RequestMapping("/employee")

public class EmployeeController {
	@Autowired
	private EmployeeService empService;

	@GetMapping("/add_employee")
    public String add_employee(Model model) {
    	Employee employee = new Employee();
    	employee.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
        model.addAttribute("employee", employee);
        model.addAttribute("mode", "add");
        return "info_detail/employee_profile";  // Trả về tên view
    }
	
	@PostMapping("/add_employee")
    public String add_shopper( @ModelAttribute Employee employee, @RequestParam("profileImage") MultipartFile img, ModelMap model) {		
		try {
			empService.insertEmployee(img, employee);
			model.addAttribute("message", "Add employee successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			employee = new Employee();
	    	employee.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
	        model.addAttribute("employee", employee);
	        model.addAttribute("mode", "add");
		}
		
        return "info_detail/employee_profile";  // Trả về tên view
    }
    
    @GetMapping("/view")
    public String view_employee(@RequestParam("employeeId") int employeeId, Model model) {
    	Employee employee = empService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("mode", "view");
        return "info_detail/employee_profile";  // Trả về tên view
    }
    
    @GetMapping("/edit")
    public String edit_employee(@RequestParam("employeeId") int employeeId, Model model) {
    	Employee employee = empService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("mode", "edit");
        return "info_detail/employee_profile";  // Trả về tên view
    }	
	@PostMapping("/edit")
    public String edit_employee( @ModelAttribute Employee employee, @RequestParam("profileImage") MultipartFile img, ModelMap model, @RequestParam("employeeId") int employeeId) {		
		try {
			employee.setId(employeeId);
			empService.updateEmployee(img, employee);
			model.addAttribute("message", "Update employee successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			employee = empService.findEmployeeById(employeeId);
	    	model.addAttribute("employee", employee);
	        model.addAttribute("mode", "edit");
		}
		
        return "info_detail/employee_profile";  // Trả về tên view
    }
    @GetMapping("/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10Employees(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Employee> employees = empService.getTop10Employees(pageNumber, isFinal);
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
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable String id) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	Employee emp = new Employee();
    	emp.setId(Integer.parseInt(id));
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	empService.deleteEmployee(emp);
            response.put("status", "success");
            response.put("message", "Employee deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("id", id);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}	
    	
    }
    
    @GetMapping("/search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopEmployee(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Employee> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = empService.getEmployeesByFullName(search_name,pageNum, isFinal);
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
