package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaPeriodoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FlujoFondoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private FlujoFondoService flujoFondoService;


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
     *  AGND1            50      50
     *  AGND2            50      50
     * FLUJO FONDOS      800     800
     */

    @Test
    public void calcularCuentas_conCuentasValidasYSinCalcularImpuestos_devuelveVoConFlujoDeFondosSinImpuestos() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Cuenta cuentaIAI1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "IAI1", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI1, new BigDecimal(1000), 2).build(em);

        Cuenta cuentaIAI2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "IAI2", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(200), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaIAI2, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI1 = CuentaBuilder.deProyectoConDescripcion(proyecto, "EAI1", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 1).build(em);
        CuentaPeriodoBuilder.deCuentaConMonto(cuentaEAI1, new BigDecimal(100), 2).build(em);

        Cuenta cuentaEAI2 = CuentaBuilder.deProyectoConDescripcion(proyecto, "EAI2", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS).build(em);
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


        Map<String, AgrupadorVo> resultadoVo = flujoFondoService.calcularCuentas(proyecto.getId(), 2, 0L);

        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("700"));
        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("0"));
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("700"));
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(0).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("50"));
        assertThat(resultadoVo.get(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name()).getCuentas().get(1).getCuentasPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("50"));
        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo()).hasSize(2);
        assertThat(resultadoVo.get(TipoFlujoFondo.FLUJO_DE_FONDOS.name()).getMontosPeriodo().stream().filter(c -> c.getPeriodo().equals(1)).findFirst().get().getMonto()).isEqualTo(new BigDecimal("800"));

    }

}
