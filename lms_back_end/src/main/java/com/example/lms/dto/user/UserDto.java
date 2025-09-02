package com.example.lms.dto.user;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}