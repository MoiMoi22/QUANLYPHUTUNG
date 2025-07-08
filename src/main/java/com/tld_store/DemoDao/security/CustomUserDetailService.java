package com.tld_store.DemoDao.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dao.EmployeeDAO;
import dto.Employee;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private EmployeeDAO empDAO;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee emp = empDAO.getEmployeeByUserName(username);
		if(emp == null) throw new UsernameNotFoundException("User hoặc password không đúng");
		return new User(emp.getEmail(), emp.getPassword(), Collections.singleton(() -> "ROLE_"+emp.getRole()));
	}

}
