package com.example.demo.Repositorios;

import com.example.demo.Entidades.ActualizacionCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoActualizacionCosto extends JpaRepository<ActualizacionCosto, Long> {
    @Query(value = "SELECT * FROM actualizacion_costo WHERE idArticuloInsumo = :idArticuloInsumo ORDER BY fechayhora DESC LIMIT 1", nativeQuery = true)
    ActualizacionCosto findUltimaActualizacion(@Param("idArticuloInsumo") Long idArticuloInsumo);
}
