package com.coop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.coop.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	// userDetailsService 사용 추가 
	private final CustomUserDetailsService userDetailsService;
	
	@Autowired
	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth //인증, 인가 설정 
            		.requestMatchers( //특정 url 엑세스 설정 
            				new AntPathRequestMatcher("/"),
            				new AntPathRequestMatcher("/auth/login"),
            				new AntPathRequestMatcher("/auth/signup"),
            				new AntPathRequestMatcher("/css/**"),
            			    new AntPathRequestMatcher("/js/**"),
            			    new AntPathRequestMatcher("/images/**"),
            			    new AntPathRequestMatcher("/error/**")
            				).permitAll() //위 요청이 오면 인증, 인가 없이 접근 가능 
            				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN") //관리자만 해당 페이지를 갈 수 있게 처
            				.anyRequest().authenticated()
            				)
            .formLogin(form -> form //폼 기반 로그인 설정 
            		.loginPage("/auth/login")  //로그인 페이지 경로 설정 
            		.defaultSuccessUrl("/index") //로그인 시 이동 경로 
            		.usernameParameter("username")
            		.passwordParameter("password")
            		)
            .logout(logout -> logout
            		.logoutSuccessUrl("/auth/login") //로그아웃 시 이동할 경로 
            		.invalidateHttpSession(true)  //로그아웃 후 세션을 삭제할지 여부
            		)
            .csrf(AbstractHttpConfigurer::disable); //테스트를 위해 csrf 비활성화
        return http.build();
    }
}