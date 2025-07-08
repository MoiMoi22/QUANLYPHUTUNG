package com.tld_store.DemoDao.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Lấy danh sách các vai trò của người dùng
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        // Chuyển hướng đến URL khác tùy thuộc vào vai trò
        String redirectUrl = determineTargetUrl(role);

        // Chuyển hướng đến URL đích
        response.sendRedirect(redirectUrl);
    }

    // Hàm xác định URL dựa trên vai trò
    private String determineTargetUrl(String role) {
        if (role.equals("ROLE_MANAGER")) {
            return "/dashboard"; // URL cho quản lý
        }
        if (role.equals("ROLE_ACCOUNTANT")) {
            return "/accountant"; // URL cho kế toán
        }
        if (role.equals("ROLE_WAREHOUSE_STAFF")) {
        	return "/warehouseStaff"; // Url cho staff
        }
        else {
        	return "/order/order_maker"; // Url cho sales
        }
    }
}
