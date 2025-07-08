package com.tld_store.DemoDao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.EmployeeDAO;
import dto.Employee;
import exception.CustomException;

@Service
public class LoginService {
	
	@Autowired
	EmployeeDAO employeeDAO;
	
	public Employee checkPassword(String userName, String passWord) {
		Employee emp;
		emp = employeeDAO.checkPassword(userName, passWord);
		
		if (emp == null) {
			throw new CustomException("Username hoặc password không đúng");
		}
		return emp;
	}
}
