package com.tld_store.DemoDao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dto.PeopleTemplate;
import dto.Product;
import dto.Supplier;
import exception.CustomException;

@Service
public class InputService {
	@Autowired
	PasswordEncoder pwEncoder;
	// Valid Họ và tên đệm
	public boolean isValidLastname(String input) {
	    // Kiểm tra chuỗi không rỗng và không chứa ký tự đặc biệt hoặc số
	    return input != null && input.matches("^[a-zA-ZÀ-ỹ\\s]+$") && !input.trim().isEmpty();
	}
	// Format theo chuẩn tên họ bình thường
	public String formatName(String input) {

	    String[] words = input.trim().split("\\s+");
	    StringBuilder formattedName = new StringBuilder();

	    for (String word : words) {
	        formattedName.append(Character.toUpperCase(word.charAt(0)))
	                     .append(word.substring(1).toLowerCase())
	                     .append(" ");
	    }
	    return formattedName.toString().trim();
	}
	// Format ID lấy chữ cái ban đầu của mỗi từ
	public String formatID(String input) {

	    String[] words = input.trim().split("\\s+");
	    StringBuilder formattedID = new StringBuilder();

	    for (String word : words) {
	    	formattedID.append(Character.toUpperCase(word.charAt(0)));
	    }
	    return formattedID.toString().trim();
	}
	
	public boolean isSingleWord(String input) {
	    return input != null && input.matches("^[a-zA-ZÀ-ỹ]+$");
	}
	
	public boolean isValidEmail(String input) {
		return input != null && input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
	}
	
	public boolean isValidPhoneNum(String input) {
		return input != null && input.matches("\\d{10}");
	}
	
	public boolean isValidPeopleInput(PeopleTemplate people) {

		if(!isValidLastname(people.getLastName())) {
			throw new CustomException("Họ và tên đệm không hợp lệ!");
		}
		if(!isSingleWord(people.getFirstName())) {
			throw new CustomException("Tên không hợp lệ");
		}
		if(!isValidEmail(people.getEmail())) {
			throw new CustomException("Email không hợp lệ!");
		}
		if(!isValidPhoneNum(people.getPhoneNum())) {
			throw new CustomException("SĐT không hợp lệ!");
		}
		if(people.getDob() == null) {
			throw new CustomException("Ngày tháng năm sinh không hợp lệ!");
		}
		return true;
	}
	
	public void normalizeName(PeopleTemplate people) {
		people.setFirstName(formatName(people.getFirstName()));
		people.setLastName(formatName(people.getLastName()));

	}
	
	public boolean isValidNameInput(String input) {
		if(!isValidLastname(input)) {
			throw new CustomException("Tên không hợp lệ!");
		}
		return true;
	}
	
	public boolean isValidProduct(Product product) {
		if(!isValidLastname(product.getProductName())) {
			throw new CustomException("Tên không hợp lệ!");
		}
		if(!isValidQuantity(product.getQuantity())) {
			throw new CustomException("Số lượng không hợp lệ!");
		}
		if(!isValidShelfLife(product.getShelfLife())) {
			throw new CustomException("Thời gian bảo hành không hợp lệ!");
		}
		if(!isValidWeight(product.getWeight())) {
			throw new CustomException("Khối lượng không hợp lệ!");
		}
		if(!isValidPrice(product.getPrice())) {
			throw new CustomException("Giá bán không hợp lệ!");
		}
		return true;
	}
	
	public boolean isValidQuantity(int input)
	{
		return input >= 0;
	}
	
	public boolean isValidWeight(float input) {
		return input > 0;
	}
	
	public boolean isValidShelfLife(int input) {
		return input >= 0;
	}
	
	public boolean isValidPrice(float input) {
		return input > 0;
	}
	
	public boolean isValidSupplierInput(Supplier supplier) {
		if(!isValidLastname(supplier.getContactName())) {
			throw new CustomException("Họ và tên không hợp lệ!");
		}
		if(!isValidLastname(supplier.getContactTitle())) {
			throw new CustomException("Chức danh không hợp lệ");
		}
		if(!isValidEmail(supplier.getEmail())) {
			throw new CustomException("Email không hợp lệ!");
		}
		if(!isValidPhoneNum(supplier.getPhone())) {
			throw new CustomException("SĐT không hợp lệ!");
		}
		return true;
	}
	
	public void isValidPassword(String password, String verifyPassword) {	
		if(!pwEncoder.matches(password, verifyPassword)) {
			throw new CustomException("Mật khẩu không giống nhau!");
		}
	}
	
}
