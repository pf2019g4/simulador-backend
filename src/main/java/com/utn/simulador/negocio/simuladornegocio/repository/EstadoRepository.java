package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Estado findByActivoTrueAndEsForecast(boolean esForecast);

    Estado findByProyectoIdAndActivoTrueAndEsForecast(Long idProyecto, boolean esForecast);

    Optional<List<Estado>> findByProyectoIdAndEsForecast(Long idProyecto, boolean esForecast);

    @Transactional
    void deleteByProyectoIdAndEsForecast(Long idProyecto, boolean esForecast);

    @Transactional
    void deleteByProyectoId(Long proyectoId);
}
