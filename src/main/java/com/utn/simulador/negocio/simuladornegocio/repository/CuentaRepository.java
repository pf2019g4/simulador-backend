package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByProyectoIdAndTipoFlujoFondo(Long id, TipoFlujoFondo tipo);

    List<Cuenta> findByProyectoIdAndTipoBalance(Long proyectoId, TipoBalance tipo);

    List<Cuenta> findByProyectoIdAndTipoFlujoFondoAndTipoBalance(Long proyectoId, TipoFlujoFondo tipoFlujoFondo, TipoBalance tipoBalance);

    List<Cuenta> findByProyectoIdAndTipoCuenta(Long proyectoId, TipoCuenta tipo);

    List<Cuenta> findByProyectoIdAndTipoCuentaAndTipoTransaccion(Long proyectoId, TipoCuenta tipo, TipoTransaccion tipoTransaccion);

    void deleteByProyectoId(Long proyectoId);
}
