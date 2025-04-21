package com.coop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.coop.dto.signupDTO;
import com.coop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signupSubmit(signupDTO signupDto, Model model) {
        boolean success = userService.signup(signupDto, model);
        if (success) {
            return "welcome";  // welcome.html로 이동
        } else {
            return "signup";   // 실패 시 signup.html에 메시지 전달
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginSubmit(String username, String password, Model model) {
        boolean success = userService.login(username, password);
        if (success) {
            return "redirect:/home";
        } else {
            model.addAttribute("message", "아이디 또는 비밀번호가 틀립니다.");
            return "login";
        }
    }
}
