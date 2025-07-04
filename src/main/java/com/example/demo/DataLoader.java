// src/main/java/com/example/demo/DataLoader.java
package com.example.demo;

import com.example.demo.Domain.Entities.Rol;
import com.example.demo.Domain.Repositories.RepoRol;
import com.example.demo.Domain.Service.Auth.UserAuth0Service; // Importa tu servicio de Auth0
import com.auth0.exception.Auth0Exception; // Importa la excepción de Auth0
import com.auth0.json.mgmt.Role; // Importa la clase Role de Auth0

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RepoRol repoRol;
    private final UserAuth0Service userAuth0Service; // Inyecta el servicio de Auth0

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando sincronización de roles desde Auth0...");

        try {
            // 1. Obtener todos los roles de Auth0
            List<Role> auth0Roles = userAuth0Service.getAllAuth0Roles();

            for (Role auth0Role : auth0Roles) {
                // 2. Buscar si el rol ya existe en la BD local por su ID de Auth0
                Optional<Rol> existingRole = repoRol.findByAuth0RoleId(auth0Role.getId());

                if (existingRole.isEmpty()) {
                    // 3. Si no existe, crear y guardar el nuevo rol en la BD local
                    Rol newRole = Rol.builder()
                            .auth0RoleId(auth0Role.getId())
                            .nombre(auth0Role.getName()) // Auth0 'name' es el nombre legible del rol
                            .build();
                    repoRol.save(newRole);
                    System.out.println("--> Nuevo rol sincronizado: '" + newRole.getNombre() + "' (Auth0 ID: " + newRole.getAuth0RoleId() + ")");
                } else {
                    // Opcional: Si el rol ya existe, puedes actualizar su nombre si cambió en Auth0.
                    // Esto evita problemas si editas el nombre de un rol en Auth0.
                    Rol roleToUpdate = existingRole.get();
                    if (!roleToUpdate.getNombre().equals(auth0Role.getName())) {
                        roleToUpdate.setNombre(auth0Role.getName());
                        repoRol.save(roleToUpdate);
                        System.out.println("--> Rol actualizado: '" + roleToUpdate.getNombre() + "' (Auth0 ID: " + roleToUpdate.getAuth0RoleId() + ")");
                    } else {
                        System.out.println("--> Rol ya existente y actualizado: '" + roleToUpdate.getNombre() + "' (Auth0 ID: " + roleToUpdate.getAuth0RoleId() + ")");
                    }
                }
            }
            System.out.println("Finalizada sincronización de roles desde Auth0.");

        } catch (Auth0Exception e) {
            System.err.println("Error al sincronizar roles desde Auth0: " + e.getMessage());
            // Puedes lanzar la excepción o simplemente loguearla, dependiendo de si quieres que la aplicación falle al iniciar si Auth0 no está disponible.
            throw new RuntimeException("Fallo al cargar roles desde Auth0 al inicio.", e);
        }
    }
}