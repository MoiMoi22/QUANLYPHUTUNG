package com.tld_store.DemoDao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tld_store.DemoDao.service.AddressService;
import com.tld_store.DemoDao.service.StatisticService;

import dto.CommuneWard;
import dto.District;
import dto.Province;
import dto.StatisticObject;
import exception.CustomException;

@RestController
@RequestMapping("/api")
public class AddressController {
	@Autowired
	AddressService addService;
	
	@Autowired
	StatisticService statisticService;
	
    // API lấy danh sách tỉnh
    @GetMapping("/provinces")
    public ArrayList<Map<String, String>> getProvinces() {
    	ArrayList<Province> provinces = addService.getAllProvince();
    	ArrayList<Map<String, String>> arr = new ArrayList<>();
    	for(Province province:provinces) {
    		Map<String, String> m = new HashMap<>();
    		m.put(String.valueOf(province.getProvinceId()), province.getProvinceName());
    		arr.add(m);
    	}
        return arr;
    }

    // API lấy danh sách huyện theo tỉnh
    @GetMapping("/districts")
    public List<Map<String, String>> getDistricts(@RequestParam int provinceId) {
    	ArrayList<District> districts =  addService.getAllDistrictByProvinceId(provinceId);
    	ArrayList<Map<String, String>> arr = new ArrayList<>();
    	for(District district:districts) {
    		Map<String, String> m = new HashMap<>();
    		m.put(String.valueOf(district.getDistrictId()), district.getDistrictName());
    		arr.add(m);
    	}
        return arr;
    }

    // API lấy danh sách xã/phường theo huyện
    @GetMapping("/wards")
    public List<Map<String, String>> getCommuneWards(@RequestParam int districtId) {
    	ArrayList<CommuneWard> wards =  addService.getAllCommuneWardByDistrictId(districtId);
    	ArrayList<Map<String, String>> arr = new ArrayList<>();
    	for(CommuneWard commune:wards) {
    		Map<String, String> m = new HashMap<>();
    		m.put(String.valueOf(commune.getCommuneId()), commune.getCommuneName());
    		arr.add(m);
    	}
        return arr;
    }
    
    @PostMapping("/chart-data")
    public List<StatisticObject> getChartData(@RequestBody Map<String, String> request) {
        // Lấy các giá trị từ request
        String startDate = request.get("startDate");
        String endDate = request.get("endDate");
        String type = request.get("timeFilter");
        System.out.println(type+"dung");
        
        try {
        	if (type.equals("day"))
        		return statisticService.getByDate(startDate, endDate);
        	else if (type.equals("month"))
        		return statisticService.getByMonth(startDate, endDate);
        	else
        		return statisticService.getByYear(startDate, endDate);
        }catch (CustomException e) {
        	System.out.println("Loi: " + e.getMessage());
        	return null;
        }
        
    }
    
    @GetMapping("/role")
    public ResponseEntity<String> getUserRole() {
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Kiểm tra nếu người dùng đã đăng nhập
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy role từ danh sách Authorities (chỉ lấy role đầu tiên)
            String role = authentication.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .findFirst()
                                        .orElse("ROLE_UNKNOWN");
            return ResponseEntity.ok(role);
        }
        // Trả về "unauthorized" nếu không có Authentication
        return ResponseEntity.status(401).body("User is not authenticated");
    }
        
}
