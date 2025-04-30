//package com.coop.entity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp; 
//
//import java.time.LocalDateTime; // 최신 시간
//
//@Getter
//@Setter
//@NoArgsConstructor // JPA 엔티티 기본 생성자가 필요합니다.
//@AllArgsConstructor
//@Builder
//@Entity 
//@Table(name = "chat_message") // 데이터베이스 테이블 이름을 "chat_message"로 지정합니다.
//public class ChatEntity {
//    @Id // 이 필드는 PK로 명시됨
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스의 auto-increment 
//    @Column(name = "chat_id") // 데이터베이스 컬럼명 "chat_id" 매핑합니다.
//    private Long chatId; 
//
//    // User 엔티티와 다대일(N:1) 관계 설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼명을 "user_id"로 지정, null 비허용
//    private UserEntity user; // 실제 User 엔티티 객체
//    // Project 엔티티와의 다대일(N:1) 관계 설정
//    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
//    @JoinColumn(name = "project_id", nullable = false) // 외래 키 컬럼명을 "project_id"로 지정, null 비허용
//    private ProjectEntity project; // 실제 Project 엔티티 객체
//
//    @Column(name = "message", columnDefinition = "TEXT", nullable = false) // 컬럼명 "message", DB 타입을 TEXT로 지정, null 비허용
//    private String message;
//
//    @CreationTimestamp // 엔티티가 생성될 때 현재 시간이 자동으로 저장됩니다. (Hibernate 기능)
//    @Column(name = "timestamp", nullable = false, updatable = false) // 컬럼명 "timestamp", null 비허용, 업데이트 불가
//    private LocalDateTime timestamp; // DB의 timestamp 타입과 매핑
//
//}
