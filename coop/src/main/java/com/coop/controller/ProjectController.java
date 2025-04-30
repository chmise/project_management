package com.coop.controller;

import com.coop.dto.ProjectDTO;
import com.coop.entity.ProjectEntity;
import com.coop.entity.UserEntity;
import com.coop.repository.ProjectRepository;
import com.coop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	

    private final ProjectRepository projectRepository;
    private final UserRepository    userRepository;

    public ProjectController(ProjectRepository projectRepository,
                             UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository    = userRepository;
    }

    // —————————————— 프로젝트 목록 조회 ——————————————
    @GetMapping
    public List<ProjectDTO> list(Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        return projectRepository.findByOwner(user).stream()
            .map(p -> ProjectDTO.builder()
                .projectId(p.getProjectId())
                .projectName(p.getProjectName())
                .ownerId(user.getId())
                .createDate(p.getCreateDate())
                .build()
            )
            .collect(Collectors.toList());
    }

    // —————————————— 추가 (기존) ——————————————
    @PostMapping("/add")
    public ResponseEntity<ProjectDTO> add(@RequestBody ProjectDTO dto,
                                          Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectEntity saved = projectRepository.save(
            ProjectEntity.builder()
                .projectName(dto.getProjectName())
                .owner(user)
                .createDate(LocalDateTime.now())
                .build()
        );

        return ResponseEntity.ok(
        	    ProjectDTO.builder()
        	        .projectId(saved.getProjectId())
        	        .projectName(saved.getProjectName())
        	        .ownerId(user.getId())
        	        .createDate(saved.getCreateDate())
        	        .build()
        	);
    }

    // —————————————— 수정 ——————————————
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(
            @PathVariable Integer id,
            @RequestBody ProjectDTO dto,
            Principal principal) {

        UserEntity user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectEntity existing = projectRepository.findById(id)
            .filter(proj -> proj.getOwner().equals(user))
            .orElseThrow(() -> new RuntimeException("Project not found or no permission"));

        // 빌더로 수정된 Entity 생성
        ProjectEntity updated = ProjectEntity.builder()
            .projectId(id)                       // ID 유지
            .projectName(dto.getProjectName())   // 변경된 이름
            .owner(user)                         // 소유자 그대로
            .createDate(existing.getCreateDate())// 생성일 그대로
            .build();

        ProjectEntity saved = projectRepository.save(updated);

        ProjectDTO result = ProjectDTO.builder()
            .projectId(saved.getProjectId())
            .projectName(saved.getProjectName())
            .ownerId(user.getId())
            .createDate(saved.getCreateDate())
            .build();

        return ResponseEntity.ok(result);
    }

    // —————————————— 삭제 ——————————————
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectEntity p = projectRepository.findById(id)
            .filter(proj -> proj.getOwner().equals(user))
            .orElseThrow(() -> new RuntimeException("Project not found or no permission"));

        projectRepository.delete(p);
        return ResponseEntity.noContent().build();
    }
    
}
