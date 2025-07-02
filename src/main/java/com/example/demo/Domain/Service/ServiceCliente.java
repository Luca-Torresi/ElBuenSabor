package com.example.demo.Domain.Service;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Usuario.ClienteRegistroDto;
import com.example.demo.Application.DTO.Usuario.PasswordChangeDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Rol;
import com.example.demo.Domain.Repositories.RepoCliente;
import com.example.demo.Domain.Repositories.RepoImagen;
import com.example.demo.Domain.Repositories.RepoRol;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceCliente extends ServiceUsuario<Cliente> {

    private final RepoCliente repoCliente;

    public ServiceCliente(RepoCliente repoCliente,
                          RepoRol repoRol,
                          UserAuth0Service userAuth0Service,
                          UserBBDDService userBBDDService, RepoImagen repoImagen) {
        super(userAuth0Service, userBBDDService, repoRol, repoImagen);
        this.repoCliente = repoCliente;
    }

    /**
     * Registrar un nuevo cliente (auto-registro).
     * Aquí se maneja la asignación del rol "CLIENTE" en Auth0 y creación en BBDD local.
     */
    @Transactional
    public Cliente registrarNuevoCliente(ClienteRegistroDto clienteDto) {
        try {
            User auth0User = userAuth0Service.getUserById(clienteDto.getAuth0Id());

            if (auth0User == null) {
                throw new RuntimeException("Usuario no encontrado en Auth0.");
            }

            System.out.println(auth0User.getId());
            Rol clienteRol = repoRol.findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado."));

            userAuth0Service.assignRoles(auth0User.getId(), List.of(clienteRol.getAuth0RoleId()));

            Set<Rol> roles = new HashSet<>();
            roles.add(clienteRol);
            return crearClienteDesdeDTO(clienteDto, dto -> {
                Cliente c = new Cliente();
                c.setRol(roles.iterator().next());
                return c;
            });

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar nuevo cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el número de teléfono de un cliente en la base de datos local.
     */
    @Transactional
    public Cliente actualizarTelefono(String auth0Id, String nuevoTelefono) {
        Cliente clienteExistente = repoCliente.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID de Auth0: " + auth0Id));

        if (nuevoTelefono == null || nuevoTelefono.isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono no puede ser nulo o vacío."); // Esto lo puedes eliminar si confías en @Valid
        }

        clienteExistente.setTelefono(nuevoTelefono);
        return repoCliente.save(clienteExistente);
    }

    /**
     * Actualiza la contraseña del cliente directamente en Auth0.
     * Esto debería usarse con cautela, principalmente en flujos administrativos o muy controlados.
     * @param auth0Id El ID de Auth0 del usuario.
     * @param passwordChangeDto DTO que contiene la nueva contraseña.
     * @return El cliente actualizado.
     * @throws RuntimeException si hay un problema con Auth0.
     */
    @Transactional // Aunque el cambio es en Auth0, la transacción asegura la integridad si hubiera cambios locales
    public Cliente actualizarContrasenaDirectamente(String auth0Id, PasswordChangeDto passwordChangeDto) {
        if (passwordChangeDto.getNewPassword() == null || passwordChangeDto.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser nula o vacía.");
        }

        try {
            // Actualiza la contraseña en Auth0
            userAuth0Service.updatePasswordDirectly(auth0Id, passwordChangeDto.getNewPassword());

            // Opcional: Si tu entidad Cliente tuviera algún campo relacionado con la contraseña
            // (ej. fecha de último cambio de contraseña), lo actualizarías aquí.
            // Cliente clienteExistente = repoCliente.findByIdAuth0(auth0Id)
            //         .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID de Auth0: " + auth0Id));
            // clienteExistente.setFechaUltimoCambioContrasena(LocalDateTime.now());
            // return repoCliente.save(clienteExistente);

            // Dado que solo se cambia en Auth0, podemos simplemente retornar el cliente existente
            // o buscarlo para asegurar que esté actualizado si fuera necesario.
            return repoCliente.findByIdAuth0(auth0Id)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado después de actualizar contraseña."));

        } catch (Auth0Exception e) {
            throw new RuntimeException("Error al actualizar contraseña directamente en Auth0: " + e.getMessage(), e);
        }
    }



    /**
     * Obtener perfil del cliente por Auth0 ID.
     */
    public Optional<Cliente> obtenerMiPerfil(String auth0Id) {
        System.out.println(auth0Id);
        return repoCliente.findByIdAuth0(auth0Id);
    }

    /**
     * Actualizar perfil del cliente.
     * Actualiza tanto en Auth0 como en base de datos local.
     */
    @Transactional
    public Cliente actualizarMiPerfil(String auth0Id, UsuarioDTO usuarioDTO) {
        Cliente clienteExistente = repoCliente.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID de Auth0: " + auth0Id));

        try {
            // Actualizar en Auth0
            userAuth0Service.modifyUser(UsuarioDTO.builder()
                    .auth0Id(auth0Id)
                    .email(usuarioDTO.getEmail())
                    .nombre(usuarioDTO.getNombre())
                    .apellido(usuarioDTO.getApellido())
                    .build());

            // Actualizar en BBDD local (solo campos permitidos)
            if (usuarioDTO.getEmail() != null) clienteExistente.setEmail(usuarioDTO.getEmail());
            if (usuarioDTO.getNombre() != null) clienteExistente.setNombre(usuarioDTO.getNombre());
            if (usuarioDTO.getApellido() != null) clienteExistente.setApellido(usuarioDTO.getApellido());
            if (usuarioDTO.getTelefono() != null) clienteExistente.setTelefono(usuarioDTO.getTelefono());

            return repoCliente.save(clienteExistente);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar perfil de cliente.", e);
        }
    }

    /**
     * Métodos para administración
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return repoCliente.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return repoCliente.findById(id);
    }
}
