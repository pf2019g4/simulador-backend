package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    public Estado findByActivoTrueAndEsForecast(boolean esForecast);

    public Estado findByProyectoIdAndActivoTrueAndEsForecast(Long idProyecto, boolean esForecast);

    public Optional<List<Estado>> findByProyectoId(Long idProyecto);
}
