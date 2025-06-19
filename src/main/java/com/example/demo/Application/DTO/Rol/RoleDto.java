package com.example.demo.Application.DTO.Rol;

import lombok.Data;

@Data
public class RoleDto {
    private String name;
    private String description;
    private String Auth0RoleId;
    private Long id;
}