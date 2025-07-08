package com.tld_store.DemoDao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
//@RequestMapping("/api")
public class HomeController {
	@Autowired
	private PasswordEncoder passEncoder;
	@GetMapping("/home")
	public String hello() {

		return "index/index";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value="message", required = false) String message, @RequestParam(value="error", required = false) Boolean error, ModelMap model) {
    	if(error!= null && error) {
    		message = "Sai mật khẩu hoặc username!";
    	}
		model.addAttribute("message", message);
		return "login/login";
	}
	

}
