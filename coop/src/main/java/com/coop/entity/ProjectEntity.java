package com.coop.entity;

import java.time.LocalDateTime; // 시간

// 어노테이션을 쓰위 위함
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//프로젝트 데이터 
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 롬북 어노테이션 필수
@Getter // 롬북 어노테이션 필드값 얻기 위해
@Entity
@Builder
@Table(name = "project")
public class ProjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Integer projectId;

	@Column(name = "project_name", nullable = false)
	private String projectName;

	@ManyToOne(fetch = FetchType.LAZY) // 관계 맵핑 애노테이션
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity owner; // user_id와 연결

	@Column(name = "create_date")
	private LocalDateTime createDate;

    public ProjectEntity(Integer projectId,
                         String projectName,
                         UserEntity owner,
                         LocalDateTime createDate) {
        this.projectId   = projectId;
        this.projectName = projectName;
        this.owner       = owner;
        this.createDate  = createDate;
    }
}
