//package com.coop.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name="project_members")
//@Data
//public class ProjectMemberEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
//	
//	@ManyToOne
//	@JoinColumn(name="user_id", nullable=false)
//	private UserEntity user;
//	
//	@ManyToOne
//	@JoinColumn(name="project_id", nullable=false)
//	private ProjectEntity project;
//	
//	public enum ProjectRole {
//		ADMIN, EDITOR, USER
//	}
//	
//	@Enumerated(EnumType.STRING)
//	@Column(nullable=false)
//	private ProjectRole role;
//}
