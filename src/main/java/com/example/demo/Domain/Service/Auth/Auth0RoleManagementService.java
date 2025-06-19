package com.example.demo.Domain.Service.Auth;

import com.example.demo.Application.DTO.Rol.Auth0RoleDto;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.Role; // Role de Auth0
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Auth0RoleManagementService {

    private final UserAuth0Service userAuth0Service;

    public Auth0RoleManagementService(UserAuth0Service userAuth0Service) {
        this.userAuth0Service = userAuth0Service;
    }

    /**
     * Obtiene todos los roles de Auth0 y los mapea a un DTO.
     * @return Lista de Auth0RoleDto.
     * @throws Auth0Exception si hay un problema al obtener los roles de Auth0.
     */
    public List<Auth0RoleDto> getAllAuth0RolesExposed() throws Auth0Exception {
        List<Role> auth0Roles = userAuth0Service.getAllAuth0Roles(); // Llama al nuevo método en UserAuth0Service
        return auth0Roles.stream()
                .map(role -> new Auth0RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }
}
