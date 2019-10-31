package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByProyectoIdAndTipoFlujoFondoAndEsForecast(Long id, TipoFlujoFondo tipo, boolean esForecast);

    List<Cuenta> findByProyectoIdAndTipoBalanceAndEsForecast(Long proyectoId, TipoBalance tipo, boolean esForecast);

    List<Cuenta> findByProyectoIdAndTipoFlujoFondoAndTipoBalanceAndEsForecast(Long proyectoId, TipoFlujoFondo tipoFlujoFondo, TipoBalance tipoBalance, boolean esForecast);

    List<Cuenta> findByProyectoIdAndTipoCuentaAndEsForecast(Long proyectoId, TipoCuenta tipo, boolean esForecast);

    List<Cuenta> findByProyectoIdAndTipoCuentaAndTipoTransaccionAndEsForecast(Long proyectoId, TipoCuenta tipo, TipoTransaccion tipoTransaccion, boolean esForecast);

    void deleteByProyectoId(Long proyectoId);
}
