package com.coop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class signupDTO {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String nickname; /*저장*/
}