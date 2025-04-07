package com.example.lostandfoundbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Tao
 * @Date 2025/4/7 20:06
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String phone;
}
