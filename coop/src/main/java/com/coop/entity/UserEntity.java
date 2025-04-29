package com.coop.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }
}