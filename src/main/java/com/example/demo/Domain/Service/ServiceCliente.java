package com.example.demo.Domain.Service;

import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO; // Usaremos UsuarioDTO para operaciones generales de cliente
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Roles;
import com.example.demo.Domain.Repositories.RepoCliente; // Inyectamos RepoCliente
import com.example.demo.Domain.Repositories.RepoRoles;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCliente {

    private final RepoCliente repoCliente; // Repositorio específico para Cliente
    private final RepoRoles repoRoles;
    private final UserAuth0Service userAuth0Service;

    // Método para que un cliente se registre (auto-registro)
    @Transactional
    public Cliente registrarNuevoCliente(UsuarioDTO usuarioDTO) {
        try {
            // Paso 0: Verificar si el usuario ya existe en Auth0 (debería venir de un login/signup previo)
            // Si el DTO ya trae un auth0Id, buscarlo en Auth0 para obtener detalles y confirmar.
            User auth0User = userAuth0Service.getUserById(usuarioDTO.getAuth0Id());
            if (auth0User == null) {
                throw new RuntimeException("Usuario no encontrado en Auth0. El registro debe seguir a un signup en Auth0.");
            }

            // Paso 1: Asignar el rol "CLIENTE" en Auth0
            Roles clienteRol = repoRoles.findByName("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en la base de datos local."));

            // Si el usuario ya tiene roles, es mejor asegurar que solo tenga el rol 'CLIENTE' si es un nuevo registro puro.
            // Para simplicidad en el auto-registro, asumimos que se le asigna solo CLIENTE.
            List<String> rolesAuth0Ids = List.of(clienteRol.getAuth0RoleId());
            userAuth0Service.assignRoles(auth0User.getId(), rolesAuth0Ids);

            // Paso 2: Crear el Cliente en la base de datos local
            Set<Roles> rolesParaBD = new HashSet<>();
            rolesParaBD.add(clienteRol);

            Cliente nuevoCliente = Cliente.builder()
                    .idAuth0(auth0User.getId())
                    .email(auth0User.getEmail() != null ? auth0User.getEmail() : usuarioDTO.getEmail())
                    .nombre(usuarioDTO.getNombre() != null ? usuarioDTO.getNombre() : auth0User.getName()) // Auth0 puede tener el nombre
                    .apellido(usuarioDTO.getApellido())
                    .telefono(usuarioDTO.getTelefono())
                    .activo(true) // Un cliente recién registrado está activo por defecto
                    .roles(rolesParaBD)
                    // Puedes añadir aquí la dirección inicial si el DTO la incluye
                    // .direccion(null) // O mapear desde DTO si aplica
                    .build();

            return repoCliente.save(nuevoCliente);

        } catch (Exception e) {
            System.err.println("Error al registrar nuevo cliente: " + e.getMessage());
            throw new RuntimeException("Error al registrar nuevo cliente: " + e.getMessage());
        }
    }

    // Método para que un cliente obtenga su propio perfil (por idAuth0)
    public Optional<Cliente> obtenerMiPerfil(String auth0Id) {
        // Asume que tienes un método findByIdAuth0 en RepoCliente o creas uno en ServiceUsuario si es más general
        // O buscas por ID de Auth0 en el repositorio de Usuario y luego casteas, si la herencia lo permite.
        // Lo más directo sería:
        return repoCliente.findByIdAuth0(auth0Id); // Necesitarías añadir este método a RepoCliente
        // Optional<Cliente> findByIdAuth0(String idAuth0);
    }


    // Método para que un cliente actualice su propio perfil
    @Transactional
    public Cliente actualizarMiPerfil(String auth0Id, UsuarioDTO usuarioDTO) {
        Cliente clienteExistente = repoCliente.findByIdAuth0(auth0Id) // <-- Usar findByIdAuth0
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID de Auth0: " + auth0Id));

        try {
            // Actualizar campos en Auth0 (si el cliente puede cambiar su email/nombre en Auth0)
            userAuth0Service.modifyUser(UsuarioDTO.builder()
                    .auth0Id(auth0Id)
                    .email(usuarioDTO.getEmail()) // Cuidado si el email es la PK en Auth0
                    .nombre(usuarioDTO.getNombre())
                    .apellido(usuarioDTO.getApellido())
                    .build());

            // Actualizar campos en la base de datos local
            clienteExistente.setEmail(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : clienteExistente.getEmail());
            clienteExistente.setNombre(usuarioDTO.getNombre() != null ? usuarioDTO.getNombre() : clienteExistente.getNombre());
            clienteExistente.setApellido(usuarioDTO.getApellido() != null ? usuarioDTO.getApellido() : clienteExistente.getApellido());
            clienteExistente.setTelefono(usuarioDTO.getTelefono() != null ? usuarioDTO.getTelefono() : clienteExistente.getTelefono());
            // No permitir que el cliente actualice sus roles, idAuth0, etc., desde aquí.
            // clienteExistente.setDireccion(...); // Si el DTO permite actualizar la dirección

            return repoCliente.save(clienteExistente);

        } catch (Exception e) {
            System.err.println("Error al actualizar perfil de cliente " + auth0Id + ": " + e.getMessage());
            throw new RuntimeException("Error al actualizar perfil de cliente.", e);
        }
    }

    // Puedes añadir métodos para la gestión de baja lógica por parte del ADMIN aquí también,
    // o centralizarlo en un ServiceUsuario si aplica a cualquier tipo de usuario.
    @Transactional
    public void altaBajaLogicaCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        cliente.setActivo(!cliente.getActivo()); // Invertir el estado
        repoCliente.save(cliente);

        try {
            // Sincronizar con Auth0
            userAuth0Service.blockUser(cliente.getIdAuth0(), !cliente.getActivo());
        } catch (Exception e) {
            System.err.println("Error al sincronizar estado de bloqueo en Auth0 para cliente " + cliente.getIdAuth0() + ": " + e.getMessage());
            throw new RuntimeException("Error al actualizar estado del cliente en Auth0.", e);
        }
    }

    // Métodos para obtener información de clientes (ej. por un ADMIN)
    public List<Cliente> obtenerTodosLosClientesActivos() {
        return repoCliente.findByActivoTrue();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return repoCliente.findById(id);
    }
}