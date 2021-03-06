package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class FlujoFondoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private FlujoFondoService flujoFondoService;

    @Autowired
    private CuentaRepository cuentaRepository;

    /*
     *                   P1      P2
     * IAI
     *  IAI1             1000    1000
     *  IAI2             200     100
     * EAI
     *  EAI1             100     100
     *  EAI2             200     100
     * GND
     *  GND1             100     100
     *  GND2             100     100
     * UTIL ANT IMP      700     700
     * IMPUESTOS         0       0
     * UTIL DPS IMP      700     700
     * AGND
     *  AGND1            100     100
     *  AGND2            100     100
     * FLUJO FONDOS      900     900
     */
    @Test
    public void calcularCuentas_conCuentasValidasYSinCalcularImpuestos_devuelveVoConFlujoDeFondosSinImpuestos() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        EstadoBuilder.inicialConPeriodoActual(proyecto, 2).build(em);

        Cuenta cuentaIAI1 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "IAI1", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 2).build(em);

        Cuenta cuentaIAI2 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "IAI2", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(200), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI1 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "EAI1", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI2 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "EAI2", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI2, new BigDecimal(200), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaGND1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "GND1", TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND1, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND1, new BigDecimal(100), 2).build(em);

        Cuenta cuentaGND2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "GND2", TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND2, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaAGND1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "AGND1", TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND1, new BigDecimal(50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND1, new BigDecimal(50), 2).build(em);

        Cuenta cuentaAGND2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "AGND2", TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND2, new BigDecimal(50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND2, new BigDecimal(50), 2).build(em);

        Map<String, AgrupadorVo> resultadoVo = flujoFondoService.calcularCuentas(proyecto.getId(), true);

        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(1);
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(1);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("700"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("0"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("700"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("100"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("100"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("900"), withinPercentage(0.001));

    }

    /*
     *                   P1      P2
     * IAI
     *  IAI1             1000    1000
     *  IAI2             200     100
     * EAI
     *  EAI1             100     100
     *  EAI2             200     100
     * GND
     *  GND1             100     100
     *  GND2             100     100
     * UTIL ANT IMP      700     700
     * IMPUESTOS         70       70
     * UTIL DPS IMP      630     630
     * AGND
     *  AGND1            100     100
     *  AGND2            100     100
     * INAI
     *  INAI1            90      90
     *  INAI2            90      90
     * ENAI
     *  ENAI1            120     120
     *  ENAI2            150     150
     * INV
     *  INV1            400     400
     *  INV2            400     400
     * FLUJO FONDOS      -60     -60
     */
    @Test
    public void calcularCuentas_conCuentasValidasYConCalcularImpuestos_devuelveVoConFlujoDeFondosConImpuestos() {

        Escenario escenario = EscenarioBuilder.escenarioConImpuesto(0.1).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConEscenario(escenario).build(em);
        EstadoBuilder.inicialConPeriodoActual(proyecto, 2).build(em);

        Cuenta cuentaIAI1 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "IAI1", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 2).build(em);

        Cuenta cuentaIAI2 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "IAI2", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(200), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI1 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "EAI1", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI2 = CuentaBuilder.deProyectoConDescripcionYTipoTransaccion(proyecto, "EAI2", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI2, new BigDecimal(200), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaGND1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "GND1", TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND1, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND1, new BigDecimal(100), 2).build(em);

        Cuenta cuentaGND2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "GND2", TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND2, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaGND2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaAGND1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "AGND1", TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND1, new BigDecimal(50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND1, new BigDecimal(50), 2).build(em);

        Cuenta cuentaAGND2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "AGND2", TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND2, new BigDecimal(50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaAGND2, new BigDecimal(50), 2).build(em);

        Cuenta cuentaINAI1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "INAI1", TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINAI1, new BigDecimal(90), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINAI1, new BigDecimal(90), 2).build(em);

        Cuenta cuentaINAI2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "INAI2", TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINAI2, new BigDecimal(90), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINAI2, new BigDecimal(90), 2).build(em);

        Cuenta cuentaENAI1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "ENAI1", TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaENAI1, new BigDecimal(120), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaENAI1, new BigDecimal(120), 2).build(em);

        Cuenta cuentaENAI2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "ENAI2", TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaENAI2, new BigDecimal(150), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaENAI2, new BigDecimal(150), 2).build(em);

        //TODO hacer inversiones en Periodo 0
        Cuenta cuentaINV1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "INV1", TipoFlujoFondo.INVERSIONES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINV1, new BigDecimal(400), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINV1, new BigDecimal(400), 2).build(em);

        //TODO hacer inversiones en Periodo 0
        Cuenta cuentaINV2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "INV2", TipoFlujoFondo.INVERSIONES).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINV2, new BigDecimal(400), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaINV2, new BigDecimal(400), 2).build(em);

        Map<String, AgrupadorVo> resultadoVo = flujoFondoService.calcularCuentas(proyecto.getId(), true);

        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(1);
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);

        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(1);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);

        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);

        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("700"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("70"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("630"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("100"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("100"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("90"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("90"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("120"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("150"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.INVERSIONES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.INVERSIONES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.INVERSIONES.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("400"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoFlujoFondo.INVERSIONES.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("400"), withinPercentage(0.001));

        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo()).hasSize(3);
        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("-60"), withinPercentage(0.001));
    }

    //TODO testar impuestos negativos
    //TODO Testear Ingresos No Afectos A Impuestos, Egresos No Afectos A Impuestos, Inversiones
    /*
     *                   P1      P2
     * VENTAS            1000    1000
     * COMPRAS           100     100
     * CM                900     900
     * otrasCuentas
     *  INTERES          100     100
     * TOTAL             800     800
     */
    @Test
    public void obteneEstadoEconomico_conCuentasValidas_devuelveVoConEstadoEconomico() {

        Escenario escenario = EscenarioBuilder.escenarioConImpuesto(0.1).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConEscenario(escenario).build(em);
        EstadoBuilder.inicialConPeriodoActual(proyecto, 2).build(em);

        Cuenta cuentaECOV1 = CuentaBuilder.deProyectoTipoEconomico(proyecto, "Ventas 1", TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOV1, new BigDecimal(400), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOV1, new BigDecimal(600), 2).build(em);

        Cuenta cuentaECOV2 = CuentaBuilder.deProyectoTipoEconomico(proyecto, "Ventas 2", TipoTransaccion.VENTA).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOV2, new BigDecimal(600), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOV2, new BigDecimal(400), 2).build(em);

        Cuenta cuentaECOC1 = CuentaBuilder.deProyectoTipoEconomico(proyecto, "Compras 1", TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOC1, new BigDecimal(-50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOC1, new BigDecimal(-50), 2).build(em);

        Cuenta cuentaECOC2 = CuentaBuilder.deProyectoTipoEconomico(proyecto, "Compras 2", TipoTransaccion.COSTO_PRODUCCION).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOC2, new BigDecimal(-50), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOC2, new BigDecimal(-50), 2).build(em);

        Cuenta cuentaECOOTRO = CuentaBuilder.deProyectoTipoEconomico(proyecto, "interes", TipoTransaccion.OTROS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOOTRO, new BigDecimal(-100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaECOOTRO, new BigDecimal(-100), 2).build(em);

        Map<String, AgrupadorVo> resultadoVo = flujoFondoService.obtenerFlujoEconomico(proyecto.getId(), true);

        assertThat(resultadoVo.get(TipoTransaccion.VENTA.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoTransaccion.VENTA.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("1000"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoTransaccion.COSTO_PRODUCCION.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoTransaccion.COSTO_PRODUCCION.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("-100"), withinPercentage(0.001));
        assertThat(resultadoVo.get("CM").getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get("CM").getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("900"), withinPercentage(0.001));
        assertThat(resultadoVo.get(TipoTransaccion.OTROS.name()).getCuentas()).hasSize(1);
        assertThat(resultadoVo.get(TipoTransaccion.OTROS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoTransaccion.OTROS.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("-100"), withinPercentage(0.001));

        assertThat(resultadoVo.get("TOTAL").getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get("TOTAL").getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isCloseTo(new BigDecimal("800"), withinPercentage(0.001));

    }

}
