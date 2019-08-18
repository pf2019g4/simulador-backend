package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaPeriodoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CuentaServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void obtenerPorProyectoYTipo_conIdProyectoYTipoValido_devuelveCuentas() {
        TipoFlujoFondo tipoFlujoFondo = TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS;
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, tipoFlujoFondo).build(em);
        CuentaPeriodoBuilder.deCuenta(cuenta, 1).build(em);
        CuentaPeriodoBuilder.deCuenta(cuenta, 2).build(em);

        List<Cuenta> cuentas = cuentaService.obtenerPorProyectoYTipoFlujoFondo(proyecto.getId(), tipoFlujoFondo);
        assertThat(cuentas).hasSize(1);
        assertThat(cuentas.get(0).getCuentasPeriodo()).hasSize(2);

    }

    @Test
    public void guardar_conCuentaNueva_guardaEnBD() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS).build();
        cuenta.getCuentasPeriodo().add(CuentaPeriodoBuilder.deCuenta(cuenta, 1).build());
        cuentaService.guardar(cuenta);

        assertThat(cuentaRepository.count()).isEqualTo(1);
    }
}
