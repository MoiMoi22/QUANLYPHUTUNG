package com.tld_store.DemoDao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tld_store.DemoDao.service.AddressService;
import com.tld_store.DemoDao.service.SupplierService;

import dto.Address;
import dto.CommuneWard;
import dto.District;
import dto.Supplier;
import exception.CustomException;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private AddressService addressService;

	@GetMapping("/add_supplier")
    public String add_supplier(ModelMap model) {
    	Supplier supplier = new Supplier();
    	supplier.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
        model.addAttribute("supplier", supplier);
        model.addAttribute("mode", "add");
        model.addAttribute("addressName", "");
        return "info_detail/supplier_profile";  // Trả về tên view
    }
	
	@PostMapping("/add_supplier")
    public String add_supplier( @ModelAttribute Supplier supplier, @ModelAttribute Address address, @RequestParam("profileImage") MultipartFile img, ModelMap model) {		
		try {
			supplierService.insertSupplier(img, supplier, address);
			model.addAttribute("message", "Add employee successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			supplier = new Supplier();
	    	supplier.setUrl("https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg");
	        model.addAttribute("supplier", supplier);
	        model.addAttribute("mode", "add");
	        model.addAttribute("addressName", "");
		}
		
        return "info_detail/supplier_profile";  // Trả về tên view
    }
	
    @GetMapping("/view")
    public String view_supplier(@RequestParam("supplierId") int supplierId, ModelMap model) {
    	Supplier supplier = supplierService.findSupplierById(supplierId);
        model.addAttribute("supplier", supplier);
        
        Address address = addressService.getAddressByAddressId(supplier.getSupplierAddressId());
        
        model.addAttribute("addressName", address.getAddressName());
        model.addAttribute("wardId", address.getCommuneWardId());
        
        CommuneWard commune = addressService.getCommuneById(address.getCommuneWardId());
        model.addAttribute("districtId", commune.getDistrictId());
        
        District district = addressService.getDistrictById(commune.getDistrictId());
        model.addAttribute("provinceId", district.getProvinceId());
        
        model.addAttribute("mode", "view");
        return "info_detail/supplier_profile";  // Trả về tên view
    }
    
    @GetMapping("/edit")
    public String edit_supplier(@RequestParam("supplierId") int supplierId, ModelMap model) {
    	Supplier supplier = supplierService.findSupplierById(supplierId);
    	
        model.addAttribute("supplier", supplier);
        
        Address address = addressService.getAddressByAddressId(supplier.getSupplierAddressId());
        
        model.addAttribute("addressName", address.getAddressName());
        model.addAttribute("wardId", address.getCommuneWardId());
        
        CommuneWard commune = addressService.getCommuneById(address.getCommuneWardId());
        model.addAttribute("districtId", commune.getDistrictId());
        
        District district = addressService.getDistrictById(commune.getDistrictId());
        model.addAttribute("provinceId", district.getProvinceId());
        
        model.addAttribute("mode", "edit");
        return "info_detail/supplier_profile";  // Trả về tên view
    }
    
    @PostMapping("/edit")
    public String edit_supplier(@RequestParam("supplierId") int supplierId, @RequestParam("profileImage") MultipartFile img, @ModelAttribute Supplier supplier, @ModelAttribute Address address, ModelMap model) {
		try {
			supplier.setSupplierId(supplierId);
			supplierService.updateSupplier(img, supplier, address);
			model.addAttribute("message", "Update supplier successfully!");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			supplier = supplierService.findSupplierById(supplierId);
	        model.addAttribute("supplier", supplier);
	        	        
	        model.addAttribute("addressName", address.getAddressName());
	        model.addAttribute("wardId", address.getCommuneWardId());
	        
	        CommuneWard commune = addressService.getCommuneById(address.getCommuneWardId());
	        model.addAttribute("districtId", commune.getDistrictId());
	        
	        District district = addressService.getDistrictById(commune.getDistrictId());
	        model.addAttribute("provinceId", district.getProvinceId());
	        model.addAttribute("mode", "edit");
		}
		
        return "info_detail/supplier_profile";  // Trả về tên view
    }
    
	
    @GetMapping("/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> getTop10Suppliers(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Supplier> suppliers = supplierService.getTop10Suppliers(pageNumber, isFinal);
        	System.out.println(suppliers.size());
            response.put("users", suppliers);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }	
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteSupplier(@PathVariable String id) {
    	Supplier supplier = new Supplier();
    	supplier.setSupplierId(Integer.parseInt(id));
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	supplierService.deleteSupplier(supplier);
            response.put("status", "success");
            response.put("message", "Supplier deleted successfully");
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
    public ResponseEntity<Map<String, Object>> findTopSupplier(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Supplier> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = supplierService.getSuppliersByFullName(search_name,pageNum, isFinal);
        	System.out.println(arr.size());
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
