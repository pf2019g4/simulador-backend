package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.jdbc.JdbcTestUtils;

public class SimuladorVentasServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    SimuladorVentasService simuladorVentasService;

    @Test
    public void simular_ventasConStock_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);
        Long stockInicial = estadoInicial.getStock();
        BigDecimal cajaInicial = estadoInicial.getCaja();

        int cantidadCuentasAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        Estado nuevoEstado = simuladorVentasService.simular(estadoInicial);

        int cantidadCuentasDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        assertThat(cantidadCuentasDespues).isEqualTo(cantidadCuentasAntes + 2);
        assertThat(cantidadCuentasPeriodosDespues).isEqualTo(cantidadCuentasPeriodosAntes + 2);
        assertThat(nuevoEstado.getId()).isEqualTo(estadoInicial.getId());
        assertThat(nuevoEstado.getStock()).isLessThan(stockInicial);
        assertThat(nuevoEstado.getCaja()).isGreaterThan(cajaInicial);
    }

}
