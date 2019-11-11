package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.BalanceBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaPeriodoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EscenarioBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Balance;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.jdbc.JdbcTestUtils;

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

        List<Cuenta> cuentas = cuentaService.obtenerPorProyectoYTipoFlujoFondo(proyecto.getId(), tipoFlujoFondo, true);
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

    @Test
    public void crearPorBalanceInicial_cuentasProveedores_creaCuentas() {

        Escenario escenario = EscenarioBuilder.base().conBalanceInicial(BalanceBuilder.balanceInicial().build()).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConEscenario(escenario).build(em);

        int cantidadCuentasProveedoresAntes = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Proveedores%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasPorCobrarAntes = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Cuentas a Cobrar%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasBancariasAntes = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Deudas Bancarias%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        cuentaService.crearPorBalanceInicial(proyecto.getId(), true);

        int cantidadCuentasProveedoresDespues = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Proveedores%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasPorCobrarDespues = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Cuentas a Cobrar%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasBancariasDespues = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cuenta", "descripcion like '%Deudas Bancarias%' and proyecto_id = " + proyecto.getId());
        int cantidadCuentasPeriodosDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        assertThat(cantidadCuentasProveedoresDespues).isEqualTo(cantidadCuentasProveedoresAntes + 2);
        assertThat(cantidadCuentasPorCobrarDespues).isEqualTo(cantidadCuentasPorCobrarAntes + 2);
        assertThat(cantidadCuentasBancariasDespues).isEqualTo(cantidadCuentasBancariasAntes + 2);
        assertThat(cantidadCuentasPeriodosDespues).isEqualTo(cantidadCuentasPeriodosAntes + 6);

    }
}
