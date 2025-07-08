package com.tld_store.DemoDao.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import dao.EmployeeDAO;
import dto.Employee;
import exception.CustomException;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeDAO empDao;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RememberPasswordService rememberPasswordService;
    @Autowired
    private InputService inputService;
    @Autowired
    private UploadImgService imgService;
    
    public Employee findEmployeeById(int id) {
    	return empDao.findById(id);
    }
    
    public Employee findEmployeeByEmail(String email) {
    	return empDao.findByEmail(email);
    }
    
	public ArrayList<Employee> getAllEmployees(ModelMap model)
	{
		ArrayList<Employee> arr = (ArrayList<Employee>)empDao.getAll();
		return arr;
	}
	
	public ArrayList<Employee> getTop10Employees(int numPage, AtomicBoolean isFinal){
		ArrayList<Employee> arr = (ArrayList<Employee>)empDao.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Employee>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public int insertEmployee(MultipartFile profileImage, Employee emp) {
		
		inputService.isValidPeopleInput(emp);
		
		inputService.normalizeName(emp);
		
		String imageUrl = "";
		int result = -1;
		
		Date d = new Date();
        String encodedPassword = passwordEncoder.encode(d.toString());
        emp.setPassword(encodedPassword);
        
        empDao.insert(emp);
		
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			imgService.checkFileUpload(profileImage);
			try {

				Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Employees"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
			    
				empDao.updateUrl(emp.getEmail(), imageUrl);
				
				String token = rememberPasswordService.generateResetToken(emp.getEmail());
				emailService.sendResetEmail(emp.getEmail(), token);
				
				result = 1;
			} 
			catch (Exception e) {
				throw new CustomException(e.getMessage());
			}		
		}
		return result;
	}
	
	public int updateEmployee(MultipartFile profileImage, Employee emp) {
		
		inputService.isValidPeopleInput(emp);
		
		inputService.normalizeName(emp);
		
		String imageUrl = "";
		int result = -1;
				
		empDao.update(emp);
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);
			try {

				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Employees"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
				empDao.updateUrl(emp.getEmail(), imageUrl);
				result = 1;
			} 
			catch (IOException e) {
				throw new CustomException(e.getMessage());
			}		
		}
		
		 return result;
	}
	
	public int deleteEmployee(Employee emp) {
		 return empDao.delete(emp);
	}
	
	public ArrayList<Employee> getEmployeesByFullName(String searchName, int numPage, AtomicBoolean isFinal)
	{
		ArrayList<Employee> arr = (ArrayList<Employee>)empDao.getTop10ByFullName(searchName.trim(), numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Employee>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public Employee findEmpByUsername(String username) {
		return empDao.getEmployeeByUserName(username);
	}
	
	public int changePassword(int id, String newPass) {
		return empDao.updatePassword(id, newPass);
	}
	
	public int changeAvatar(String username, String url) {
		return empDao.updateUrl(username, url);
	}
	
	public String getFullNameById(int empId) {
	    
	    Employee emp = findEmployeeById(empId);
	    
	    return emp.getLastName() + " " + emp.getFirstName();
	}
	
	public String getFullNameByCurrentEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy username
	        username = authentication.getName();
	    }
	    	    
	    Employee emp = findEmployeeByEmail(username);
	    
	    return emp.getLastName() + " " + emp.getFirstName();
	}
    
	public Employee getCurrentEmployee() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy username
	        username = authentication.getName();
	    }
	    	    
	    Employee emp = findEmployeeByEmail(username);
	    
	    return emp;
	}
	
}
