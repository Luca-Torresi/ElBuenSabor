package com.example.demo;

import com.example.demo.Domain.Entities.Departamento;
import com.example.demo.Domain.Entities.Provincia;
import com.example.demo.Domain.Entities.Rol;
import com.example.demo.Domain.Entities.UnidadDeMedida;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoProvincia;
import com.example.demo.Domain.Repositories.RepoRol;
import com.example.demo.Domain.Repositories.RepoUnidadDeMedida;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RepoRol repoRol;
    private final UserAuth0Service userAuth0Service;
    private final RepoUnidadDeMedida repoUnidadDeMedida;
    private final RepoProvincia repoProvincia;
    private final RepoDepartamento repoDepartamento;

    @Override
    public void run(String... args) throws Exception {
        // --- 1. SINCRONIZACIÓN DE ROLES (Tu código original) ---
        syncAuth0Roles();

        // --- 2. CARGA INICIAL DE UNIDADES DE MEDIDA ---
        loadUnidadesDeMedida();

        // --- 3. CARGA INICIAL DE UBICACIONES ---
        loadMendozaDepartments();
    }

    // Metodo extraído para mantener limpio el run()
    private void syncAuth0Roles() throws Auth0Exception {
        System.out.println("Iniciando sincronización de roles desde Auth0...");

        try {
            List<Role> auth0Roles = userAuth0Service.getAllAuth0Roles();

            for (Role auth0Role : auth0Roles) {
                Optional<Rol> existingRole = repoRol.findByAuth0RoleId(auth0Role.getId());

                if (existingRole.isEmpty()) {
                    Rol newRole = Rol.builder()
                            .auth0RoleId(auth0Role.getId())
                            .nombre(auth0Role.getName())
                            .build();
                    repoRol.save(newRole);
                    System.out.println("--> Nuevo rol sincronizado: '" + newRole.getNombre() + "' (Auth0 ID: " + newRole.getAuth0RoleId() + ")");
                } else {
                    Rol roleToUpdate = existingRole.get();
                    if (!roleToUpdate.getNombre().equals(auth0Role.getName())) {
                        roleToUpdate.setNombre(auth0Role.getName());
                        repoRol.save(roleToUpdate);
                        System.out.println("--> Rol actualizado: '" + roleToUpdate.getNombre() + "' (Auth0 ID: " + roleToUpdate.getAuth0RoleId() + ")");
                    } else {
                        System.out.println("--> Rol ya existente: '" + roleToUpdate.getNombre() + "' (Auth0 ID: " + roleToUpdate.getAuth0RoleId() + ")");
                    }
                }
            }
            System.out.println("Finalizada sincronización de roles desde Auth0.");

        } catch (Auth0Exception e) {
            System.err.println("Error al sincronizar roles desde Auth0: " + e.getMessage());
            throw e; // Relanza la excepción si quieres que falle al inicio.
        }
    }

    // Nuevo metodo para cargar las unidades de medida
    private void loadUnidadesDeMedida() {
        System.out.println("Iniciando carga inicial de unidades de medida...");

        // Lista de unidades de medida esenciales
        List<String> nombresUnidades = Arrays.asList(
                "Kilogramo (kg)",
                "Gramo (gr)",
                "Litro (lt)",
                "Mililitro (ml)",
                "Unidad (u)",
                "Docena (doz)"
        );

        // Asumiendo que tienes un metodo 'findByNombre' en RepoUnidadDeMedida
        // y que tu entidad UnidadDeMedida usa Lombok @Builder o tiene un constructor adecuado

        int count = 0;
        for (String nombre : nombresUnidades) {
            // Busca si la unidad ya existe por nombre
            Optional<UnidadDeMedida> existingUnidad = repoUnidadDeMedida.findByNombre(nombre);

            if (existingUnidad.isEmpty()) {
                // Si no existe, crea y guarda la nueva unidad
                UnidadDeMedida newUnidad = UnidadDeMedida.builder()
                        .nombre(nombre)
                        // Si tienes un campo de 'abreviatura' u otro, inicialízalo aquí
                        .build();
                repoUnidadDeMedida.save(newUnidad);
                System.out.println("--> Nueva unidad de medida cargada: " + nombre);
                count++;
            }
        }

        System.out.println("Finalizada carga de unidades de medida. (" + count + " nuevas unidades añadidas).");
    }

    // Nuevo metodo para cargar la provincia y sus departamentos
    private void loadMendozaDepartments() {
        System.out.println("Iniciando carga de la provincia de Mendoza y sus departamentos...");

        // 1. Asegurar que la provincia "Mendoza" existe
        String nombreProvincia = "Mendoza";
        Provincia mendoza = repoProvincia.findByNombre(nombreProvincia)
                .orElseGet(() -> {
                    Provincia newProvincia = Provincia.builder().nombre(nombreProvincia).build();
                    repoProvincia.save(newProvincia);
                    System.out.println("--> Provincia '" + nombreProvincia + "' creada.");
                    return newProvincia;
                });

        // 2. Lista de Departamentos de Mendoza
        List<String> nombresDepartamentos = Arrays.asList(
                "Capital", "General Alvear", "Godoy Cruz", "Guaymallén",
                "Junín", "La Paz", "Las Heras", "Lavalle",
                "Luján de Cuyo", "Maipú", "Malargüe", "Rivadavia",
                "San Carlos", "San Martín", "San Rafael", "Santa Rosa",
                "Tunuyán", "Tupungato", "Rodeo de la Cruz"
        );

        int count = 0;
        for (String nombre : nombresDepartamentos) {
            // Asumo que tienes un método findByNombreAndProvincia en RepoDepartamento
            // o simplemente confías en que el ID autoincremental evitará problemas.
            // Para simplificar, asumiremos que solo chequeamos por nombre del departamento.

            Optional<Departamento> existingDepartamento = repoDepartamento.findByNombre(nombre);

            if (existingDepartamento.isEmpty()) {
                Departamento newDepartamento = Departamento.builder()
                        .nombre(nombre)
                        .provincia(mendoza)
                        .build();
                repoDepartamento.save(newDepartamento);
                count++;
            } else {
                // Opcional: Si el departamento ya existe, puedes asegurar que esté vinculado a Mendoza si es necesario.
                // En este caso, lo dejamos simple.
            }
        }

        System.out.println("Finalizada carga de departamentos. (" + count + " nuevos departamentos de Mendoza añadidos).");
    }
}