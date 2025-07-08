package com.tld_store.DemoDao.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail; // Lấy địa chỉ email từ file cấu hình

    public void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:8080/forgot_password/reset_password?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Reset Password");
            helper.setText("<p>Click the link below to reset your password:</p>" +
                    "<a href=\"" + resetLink + "\">Reset Password</a>", true);

            mailSender.send(message);
		} catch (MessagingException e) {
			throw new CustomException(e.getMessage());
		}

    }
}

