package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CuentaPeriodoRepository extends JpaRepository<CuentaPeriodo, Long> {

    @Query("select cp from CuentaPeriodo cp where cp.cuenta.proyectoId = ?1 and cp.periodo = ?2 and cp.cuenta.esForecast = ?3 ")
    List<CuentaPeriodo> findByProyectoAndPeriodoAndEsForecast(Long proyectoId, int periodo, boolean esForecast);

    @Query("select cp from CuentaPeriodo cp where cp.cuenta.proyectoId = ?1 and cp.cuenta.esForecast = ?2 ")
    List<CuentaPeriodo> findByCuentaProyectoIdAndEsForecast(Long proyectoId, boolean esForecast);

    void deleteByCuentaProyectoId(Long proyectoId);
}
