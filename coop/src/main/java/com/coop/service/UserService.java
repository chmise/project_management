package com.coop.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.coop.dto.LoginDTO;
import com.coop.dto.SignupDTO;
import com.coop.entity.UserEntity;
import com.coop.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    // 회원 가입 저장
    public void save(SignupDTO signupDTO) {
        // 중복 사용자 확인
        if (userRepository.findByUsername(signupDTO.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        
        if (userRepository.findByEmail(signupDTO.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        
        // 새 사용자 엔티티 생성
        UserEntity user = new UserEntity();
        user.setUsername(signupDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword())); // 비밀번호 암호화
        user.setEmail(signupDTO.getEmail());
        user.setNickname(signupDTO.getNickname());
        
        // 사용자 저장
        userRepository.save(user);
    }
    
    // 사용자 정보 조회
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}