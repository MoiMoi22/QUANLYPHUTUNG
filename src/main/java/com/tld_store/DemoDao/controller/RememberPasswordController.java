package com.tld_store.DemoDao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tld_store.DemoDao.service.EmailService;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.InputService;
import com.tld_store.DemoDao.service.LoginService;
import com.tld_store.DemoDao.service.RememberPasswordService;

import dto.Employee;
import exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
@RequestMapping("/forgot_password")
public class RememberPasswordController {
	
	@Autowired
	private RememberPasswordService rememberPassService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeService empService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InputService inputService;
    
    
    @PostMapping
    public String requestReset(@RequestParam("email") String email, ModelMap model) {    	
        try {
    		if(!inputService.isValidEmail(email)) {
    			throw new CustomException("Email không hợp lệ!");
    		}
        	String token = rememberPassService.generateResetToken(email);
            emailService.sendResetEmail(email, token);
            model.addAttribute("message", "Kiểm tra mail để thay đổi mật khẩu!");
         
        } catch (CustomException e) {
        	 model.addAttribute("message", e.getMessage());
        }
        return "login/login";
    }
    @GetMapping("/reset_password")
    public String viewResetPassword(@RequestParam(value="message", required = false) String message, ModelMap model) {
    	model.addAttribute("message", message);
    	return "login/resetPassword";
    }
    
    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, ModelMap model, RedirectAttributes redirectAttributes) {

        // Logic đặt lại mật khẩu, ví dụ:
    	try {
            Employee user = rememberPassService.findEmployee(token);
            if(user == null) throw new CustomException("Token không hợp lệ!");
            
            String newPassEncoded = passwordEncoder.encode(newPassword);
            String confirmPassEncoded = passwordEncoder.encode(confirmPassword);
            
            inputService.isValidPassword(newPassword, confirmPassEncoded);
            
            user.setPassword(newPassEncoded);
            
            empService.changePassword(user.getId(), user.getPassword());
            redirectAttributes.addAttribute("message", "Thay đổi mật khẩu thành công!");
//            rememberPassService.deleteToken(token); sửa hàm
		} catch (CustomException e) {
			redirectAttributes.addAttribute("message", e.getMessage());
			redirectAttributes.addAttribute("token", token);
			if(e.getMessage().equals("Mật khẩu không giống nhau!")) {
				return "redirect:/forgot_password/reset_password";
			}
		}

        return "redirect:/login";
    }
}
