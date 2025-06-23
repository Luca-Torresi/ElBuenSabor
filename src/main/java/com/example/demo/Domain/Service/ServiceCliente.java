package com.example.demo.Domain.Service;

import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Usuario.ClienteDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Roles;
import com.example.demo.Domain.Repositories.RepoCliente;
import com.example.demo.Domain.Repositories.RepoRoles;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class ServiceCliente extends ServiceUsuario<Cliente> {

    private final RepoCliente repoCliente;

    public ServiceCliente(RepoCliente repoCliente,
                          RepoRoles repoRoles,
                          UserAuth0Service userAuth0Service,
                          UserBBDDService userBBDDService) {
        super(userAuth0Service, userBBDDService, repoRoles);
        this.repoCliente = repoCliente;
    }

    /**
     * Registrar un nuevo cliente (auto-registro).
     * Aquí se maneja la asignación del rol "CLIENTE" en Auth0 y creación en BBDD local.
     */
    @Transactional
    public Cliente registrarNuevoCliente(ClienteDto clienteDto) {
        try {
            User auth0User = userAuth0Service.getUserById(clienteDto.getAuth0Id());
            if (auth0User == null) {
                throw new RuntimeException("Usuario no encontrado en Auth0.");
            }

            Roles clienteRol = repoRoles.findByName("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado."));

            userAuth0Service.assignRoles(auth0User.getId(), List.of(clienteRol.getAuth0RoleId()));

            return crearClienteDesdeDTO(clienteDto, dto -> {
                Cliente c = new Cliente();
                Set<Roles> roles = new java.util.HashSet<>();
                roles.add(clienteRol);
                c.setRoles(roles);
                return c;
            });

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar nuevo cliente: " + e.getMessage(), e);
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
     * Alta/Baja lógica de cliente sincronizada con Auth0.
     * Aprovecha el método genérico de la clase base.
     */
    @Transactional
    public void altaBajaLogicaCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        boolean nuevoEstado = !cliente.getActivo();
        cliente.setActivo(nuevoEstado);
        repoCliente.save(cliente);

        try {
            userAuth0Service.blockUser(cliente.getIdAuth0(), !nuevoEstado);
        } catch (Exception e) {
            throw new RuntimeException("Error al sincronizar estado de bloqueo en Auth0 para cliente " + cliente.getIdAuth0(), e);
        }
    }

    /**
     * Métodos para administración
     */
    public List<Cliente> obtenerTodosLosClientesActivos() {
        return repoCliente.findByActivoTrue();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return repoCliente.findById(id);
    }
}
