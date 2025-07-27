package com.example.demo.Domain.Service.Auth;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.RolesFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.Role;
import com.auth0.json.mgmt.RolesPage;
import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Rol.Auth0RoleDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuth0Service {

    private final ManagementAPI managementAPI;

    @Value("${auth0.database.connection.id}")
    private String auth0Connection;

    // Constructor que inyecta el ManagementAPI y el Connection ID
    public UserAuth0Service(ManagementAPI managementAPI) {
        this.managementAPI = managementAPI;
    }


    public User createUser(UsuarioDTO usuarioDTO) throws Exception {
        User user = new User();
        user.setEmail(usuarioDTO.getEmail());
        user.setPassword(usuarioDTO.getPassword().toCharArray());
        user.setEmailVerified(true);
        user.setName(usuarioDTO.getNombre() + " " + usuarioDTO.getApellido());
        user.setConnection(auth0Connection);

        return managementAPI.users().create(user).execute();
    }

    public List<Auth0RoleDto> getAllRole() throws Auth0Exception {
        List<Role> auth0Roles = getAllAuth0Roles();
        return auth0Roles.stream()
                .map(role -> new Auth0RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }

    public void assignRoles(String userIdAuth0, List<String> auth0RoleIds) throws Exception {
        managementAPI.users().addRoles(userIdAuth0, auth0RoleIds).execute();
    }

    public void removeRoles(String userIdAuth0, List<String> auth0RoleIds) throws Exception {
        managementAPI.users().removeRoles(userIdAuth0, auth0RoleIds).execute();
    }

    public User modifyUser(UsuarioDTO usuarioDTO) throws Exception {
        User user = new User();
        user.setEmail(usuarioDTO.getEmail());
        user.setName(usuarioDTO.getNombre() + " " + usuarioDTO.getApellido());
        return managementAPI.users().update(usuarioDTO.getAuth0Id(), user).execute();
    }

    public User getUserById(String auth0Id) throws Exception {
        return managementAPI.users().get(auth0Id, null).execute();
    }

    public List<User> getAllUsers() throws Exception {
        return managementAPI.users().list(null).execute().getItems();
    }

    public void deleteUser(String auth0Id) throws Exception {
        managementAPI.users().delete(auth0Id).execute();
    }
    // Método para bloquear/desbloquear un usuario en Auth0
    public User blockUser(String userIdAuth0, boolean blockStatus) throws Exception {
        User user = new User();
        user.setBlocked(blockStatus);
        return managementAPI.users().update(userIdAuth0, user).execute();
    }

    // Metodo para obtener los roles actuales de un usuario en Auth0 (útil para actualizar roles)
    public List<Role> getUserRoles(String userIdAuth0) throws Exception {
        return managementAPI.users().listRoles(userIdAuth0, null).execute().getItems();
    }

    // Para actualizar roles de forma más robusta:
    public void updateRoles(String userIdAuth0, List<String> newAuth0RoleIds) throws Exception {
        // 1. Obtener roles actuales del usuario en Auth0
        List<Role> currentRoles = getUserRoles(userIdAuth0);
        List<String> currentRoleIds = currentRoles.stream()
                .map(Role::getId)
                .toList();

        // 2. Identificar roles a remover (actuales que no están en los nuevos)
        List<String> rolesToRemove = currentRoleIds.stream()
                .filter(roleId -> !newAuth0RoleIds.contains(roleId))
                .collect(Collectors.toList());

        // 3. Identificar roles a añadir (nuevos que no están en los actuales)
        List<String> rolesToAdd = newAuth0RoleIds.stream()
                .filter(roleId -> !currentRoleIds.contains(roleId))
                .collect(Collectors.toList());

        // 4. Ejecutar operaciones en Auth0
        if (!rolesToRemove.isEmpty()) {
            removeRoles(userIdAuth0, rolesToRemove);
        }
        if (!rolesToAdd.isEmpty()) {
            assignRoles(userIdAuth0, rolesToAdd);
        }
    }

    public List<Role> getAllAuth0Roles() throws Auth0Exception {
        RolesPage rolesPage = managementAPI.roles().list(new RolesFilter()).execute();
        return rolesPage.getItems();
    }

    /**
     * Actualiza la contraseña de un usuario directamente en Auth0.
     * Esta operación es para uso administrativo o flujos controlados.
     * El usuario NO recibe una notificación de email por defecto.
     * @param userIdAuth0 El ID de Auth0 del usuario.
     * @param newPassword La nueva contraseña para el usuario.
     * @return El objeto User actualizado.
     * @throws Auth0Exception si ocurre un error con la API de Auth0.
     */
    public User updatePasswordDirectly(String userIdAuth0, String newPassword) throws Auth0Exception {
        User user = new User();
        user.setPassword(newPassword.toCharArray());
        user.setConnection(auth0Connection);

        // Puedes añadir password_verify para forzar la verificación si es un flujo de admin
        // user.setPasswordVerify(true); // Requiere confirmación si tienes reglas de confirmación de password

        return managementAPI.users().update(userIdAuth0, user).execute();
    }
}
