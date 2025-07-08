//package com.tld_store.DemoDao.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//	@Autowired
//    private CustomUserDetailService customUserDetailsService; // Inject CustomUserDetailsService
//	@Autowired
//	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .csrf().disable()
//	        .authorizeRequests()
//	        .requestMatchers("/accountant/**", "purchase/purchase_maker", "purchase/createInvoice", "purchase/pay_debt", "/transaction/add_phieuthu", "/transaction/edit_phieuthu" , "/transaction/add_phieuchi", "/transaction/edit_phieuchi").hasRole("ACCOUNTANT")
//	        
//	        .requestMatchers("/warehouseStaff/**").hasRole("WAREHOUSE_STAFF")
//	        
//	        .requestMatchers("/order/order_maker/**", "/order/add_order/**").hasRole("SALES")
//	        .requestMatchers("shopper/add_shopper").hasAnyRole("SALES", "MANAGER")
//
//	        .requestMatchers("/dashboard/**", "/user/**", "/shopper/**", "/supplier/**", "employee/**", "/product/**", "/category/**", "/order/**", "purchase/**", "/transaction/**", "/statistic").hasRole("MANAGER")
//	        
//	        .requestMatchers("/login/**", "/static/**", "/forgot_password/**").permitAll()
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin()
//	        .loginPage("/login")
//	        .loginProcessingUrl("/api/auth/login")
//	        .failureUrl("/login?error=true")
//	        .successHandler(customAuthenticationSuccessHandler)  
//	        .permitAll()
//	        .and()
//	        .logout()
//	        .logoutUrl("/logout")  // Định nghĩa URL logout
//	        .logoutSuccessUrl("/login?logout=true") // Chuyển hướng sau khi logout thành công
//	        .invalidateHttpSession(true) // Hủy session sau khi đăng xuất
//	        .clearAuthentication(true) // Xóa thông tin xác thực sau khi logout
//	        .permitAll();
//	    
//	    return http.build();
//	}
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
//        // Sử dụng CustomUserDetailsService thay vì UserDetailsService mặc định
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(customUserDetailsService)  // Inject CustomUserDetailsService
//                .passwordEncoder(passwordEncoder)
//                .and()
//                .build();
//    }
//}

//////////////////////////////////////////////////////////////////////////////

package com.tld_store.DemoDao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Autowired
    private CustomUserDetailService customUserDetailsService; // Inject CustomUserDetailsService
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf().disable()
	        .authorizeRequests()
	        .requestMatchers("/admin/**").hasRole("ADMIN")
	        .requestMatchers("/accountant/**").hasRole("ACCOUNTANT")
	        .requestMatchers("/warehouseStaff/**").hasRole("WAREHOUSE_STAFF")
	        .requestMatchers("/user/**").hasAnyRole("MANAGER")
	        .requestMatchers("/api/**").authenticated()
	        .requestMatchers("/login/**", "/register", "/static/**", "/forgot_password/**", "/home/**").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/api/auth/login")
	        .failureUrl("/login?error=true")
	        .successHandler(customAuthenticationSuccessHandler)  
	        .permitAll()
	        .and()
	        .logout()
	        .logoutUrl("/logout")  // Định nghĩa URL logout
	        .logoutSuccessUrl("/login?logout=true") // Chuyển hướng sau khi logout thành công
	        .invalidateHttpSession(true) // Hủy session sau khi đăng xuất
	        .clearAuthentication(true) // Xóa thông tin xác thực sau khi logout
	        .permitAll();
	    
	    return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        // Sử dụng CustomUserDetailsService thay vì UserDetailsService mặc định
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)  // Inject CustomUserDetailsService
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}
