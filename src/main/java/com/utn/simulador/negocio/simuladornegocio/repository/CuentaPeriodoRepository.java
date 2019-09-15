package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CuentaPeriodoRepository extends JpaRepository<CuentaPeriodo, Long> {

    
    @Query("select cp from CuentaPeriodo cp where cp.cuenta.proyectoId = ?1 and cp.periodo = ?2 ")
    List<CuentaPeriodo> findByProyectoAndPeriodo(Long proyectoId, int periodo);
}
