package com.coop.service;

import com.coop.dto.signupDTO;
import com.coop.entity.UserEntity;
import com.coop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입 처리 - 메시지를 Model에 직접 설정
    public boolean signup(signupDTO signupDto, Model model) {
        if (!signupDto.getPassword().equals(signupDto.getPasswordConfirm())) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return false;
        }

        if (userRepository.findByUsername(signupDto.getUsername()) != null) {
            model.addAttribute("message", "이미 존재하는 아이디입니다.");
            return false;
        }

        if (userRepository.findByNickname(signupDto.getNickname()) != null) {
            model.addAttribute("message", "이미 존재하는 이름입니다.");
            return false;
        }

        if (userRepository.findByEmail(signupDto.getEmail()) != null) {
            model.addAttribute("message", "이미 가입된 이메일입니다.");
            return false;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signupDto.getUsername());
        userEntity.setPassword(signupDto.getPassword()); 
        userEntity.setEmail(signupDto.getEmail());
        userEntity.setNickname(signupDto.getNickname());
        userEntity.setRole(UserEntity.Role.USER);

        userRepository.save(userEntity);

        model.addAttribute("message", "회원가입을 환영합니다!");
        return true;
    }

    // 로그인 처리
    public boolean login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);  
    }
}
