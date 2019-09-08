package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByProyectoIdAndTipoFlujoFondo(Long id, TipoFlujoFondo tipo);
    List<Cuenta> findByProyectoIdAndOpcionId(Long proyectoId, Long opcionId);
}
