package com.coop.repository;

import com.coop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username); // 아이디 중복 확인
    UserEntity findByNickname(String nickname); // 닉네임 중복 확인
    UserEntity findByEmail(String email);       // 이메일 중복 확인   
}
