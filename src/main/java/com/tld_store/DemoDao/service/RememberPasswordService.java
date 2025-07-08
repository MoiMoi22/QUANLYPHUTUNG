package com.tld_store.DemoDao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.EmployeeDAO;
import dto.Employee;
import exception.CustomException;

@Service
public class RememberPasswordService {
	@Autowired
	EmployeeDAO employeeDAO;
	public int createToken(String email, String token) {
		return employeeDAO.insertToken(email, token);
	}
	
	public Employee findEmployee(String token) {
		return employeeDAO.findByToken(token);
	}
	
	public int deleteToken(String token) {
		return employeeDAO.deleteToken(token);
	}
	
    public String generateResetToken(String email) {
        String token = TokenGenerator.generateToken();
        int result = createToken(email, token);
        if(result == 0) {
        	throw new CustomException("Email không tồn tại");
        }
        return token;
    }
}
