package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimuladorServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    SimuladorService simuladorService;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CuentaPeriodoRepository cuentaPeriodoRepository;

    @Test
    public void simularPeriodo_escenarioValido_avanzaElPeriodo() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(proyecto).build(em);
        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuenta(cuenta, 1).build(em);

        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estadoInicial.getPeriodo() + 1).build(em);

        Estado nuevoEstado = simuladorService.simularPeriodo(proyecto.getId(), true);

        assertThat(nuevoEstado.getPeriodo()).isEqualTo(estadoInicial.getPeriodo() + 1);
        assertThat(nuevoEstado.getCaja()).isGreaterThan(new BigDecimal("1000"));
        assertThat(nuevoEstado.getActivo()).isTrue();

    }

 
    @Test
    public void simularPeriodos_escenarioValido_avanzaElPeriodoHastaElMaximo() {
        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        for (int i = 0; i < escenario.getMaximosPeriodos(); i++) {
            ForecastBuilder.baseDeProyectoYPeriodo(proyecto, i + 1).build(em);
        }

        simuladorService.simularPeriodos(proyecto.getId(), true);

        assertThat(estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(proyecto.getId(), true).getPeriodo()).isEqualTo(escenario.getMaximosPeriodos());

    }

    @Test
    public void deshacerSimulacionPrevia_conEscenarioPrevioCreado_eliminaCuentasAsociadosAlForecast() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        Estado estado = EstadoBuilder.inicialConPeriodoYEstado(proyecto, 0, true).build(em);

        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodo cuentaPeriodo1 = CuentaPeriodoBuilder.deCuentaYEsForecast(cuenta, 1).build(em);
        CuentaPeriodo cuentaPeriodo2 = CuentaPeriodoBuilder.deCuentaYEsForecast(cuenta, 2).build(em);

        simuladorService.deshacerSimulacionPrevia(proyecto.getId());

        assertThat(cuentaPeriodoRepository.findByCuentaProyectoIdAndEsForecast(proyecto.getId(), estado.getEsForecast())).hasSize(0);

    }

}
