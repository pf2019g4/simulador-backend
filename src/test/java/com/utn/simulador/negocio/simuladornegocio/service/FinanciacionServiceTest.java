package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.jdbc.JdbcTestUtils;

public class FinanciacionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private FinanciacionService financiacionService;

    @Test
    public void acreditar_proyectoConFinanciacionTomada_creaCuentaEconomicaYFinanciera() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        Financiacion financiacion = FinanciacionBuilder.tresCuotas(escenario).build(em);

        BigDecimal monto = new BigDecimal(10000);
        financiacionService.tomar(Credito.builder().monto(monto).financiacionId(financiacion.getId()).proyectoId(proyecto.getId()).periodoInicial(0).build());

        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");
        int cantidadCuentasCreditosAntes = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "(descripcion like '%Crédito%' or descripcion like '%Interés%' or descripcion like '%cuota%') and proyecto_id = " + proyecto.getId());

        financiacionService.acreditar(proyecto.getId(), true);

        int cantidadCuentasCreditosDespues = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "(descripcion like '%Crédito%' or descripcion like '%Interés%' or descripcion like '%cuota%') and proyecto_id = " + proyecto.getId());
        int cantidadCuentasPeriodosDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        //economicas y financieras ( interes, credito y amort. cuota)
        assertThat(cantidadCuentasCreditosDespues).isEqualTo(cantidadCuentasCreditosAntes + 6);

        //3 cuotas ( 3 cuentas para intereses y 3 para amortización crédito ) + 1 del ingreso del crédito + 3 economica
        assertThat(cantidadCuentasPeriodosDespues).isEqualTo(cantidadCuentasPeriodosAntes + 10);

    }
}
