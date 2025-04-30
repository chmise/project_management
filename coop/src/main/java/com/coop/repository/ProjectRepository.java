package com.coop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coop.entity.ProjectEntity;
import com.coop.entity.UserEntity;

import java.util.List;
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
	// 소유자(owner)로 프로젝트 리스트 조회
	List<ProjectEntity> findByOwner(UserEntity owner);
}
