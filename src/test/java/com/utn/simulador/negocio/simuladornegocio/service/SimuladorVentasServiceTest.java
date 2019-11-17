package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ModalidadCobroBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ForecastBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        Estado estadoInicial = EstadoBuilder.inicial(proyecto).build(em);
        Long stockInicial = estadoInicial.getStock();
        BigDecimal inventarioInicial = estadoInicial.getInventario();
        BigDecimal cajaInicial = estadoInicial.getCaja();

        int cantidadCuentasAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        estadoInicial.setPeriodo(estadoInicial.getPeriodo() + 1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estadoInicial.getPeriodo()).build(em);
        Estado nuevoEstado = simuladorVentasService.simular(estadoInicial, false);

        int cantidadCuentasDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        assertThat(cantidadCuentasDespues).isEqualTo(cantidadCuentasAntes + 3);
        assertThat(cantidadCuentasPeriodosDespues).isEqualTo(cantidadCuentasPeriodosAntes + 3);
        assertThat(nuevoEstado.getId()).isEqualTo(estadoInicial.getId());
        assertThat(nuevoEstado.getStock()).isLessThan(stockInicial);
        assertThat(nuevoEstado.getCaja()).isGreaterThan(cajaInicial);
        assertThat(nuevoEstado.getInventario()).isLessThan(inventarioInicial);
    }

    @Test
    public void simular_ventasConStock_Diferido_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);

        List<ModalidadCobro> modalidadesCobro = new ArrayList<>();
        modalidadesCobro.add(ModalidadCobroBuilder.base(proyecto, 60L, 0).build(em)); //60% contado
        modalidadesCobro.add(ModalidadCobroBuilder.base(proyecto, 0L, 1).build(em)); //0% a 30 dias
        modalidadesCobro.add(ModalidadCobroBuilder.base(proyecto, 40L, 2).build(em)); //40% a 60 dias
        proyecto.setModalidadCobro(modalidadesCobro);
        Estado estadoInicial = EstadoBuilder.inicial(proyecto).conStock(400L).conInventario(BigDecimal.valueOf(50000L)).build(em);
        Long stockInicial = estadoInicial.getStock();
        BigDecimal cajaInicial = estadoInicial.getCaja();

        int cantidadCuentasAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        estadoInicial.setPeriodo(estadoInicial.getPeriodo() + 1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estadoInicial.getPeriodo()).build(em);
        Estado estadoContado = simuladorVentasService.simular(estadoInicial, false);
        Long stockContado = estadoContado.getStock();
        BigDecimal cajaContado = estadoContado.getCaja();
        BigDecimal variacionCajaContado = cajaContado.subtract(cajaInicial);

        estadoContado.setPeriodo(estadoContado.getPeriodo() + 1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estadoInicial.getPeriodo()).build(em);
        Estado estado30D = simuladorVentasService.simular(estadoContado, false);
        Long stock30D = estado30D.getStock();
        BigDecimal caja30D = estado30D.getCaja();
        BigDecimal variacionCaja30D = caja30D.subtract(cajaContado);

        estado30D.setPeriodo(estado30D.getPeriodo() + 1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estadoInicial.getPeriodo()).build(em);
        Estado estado60D = simuladorVentasService.simular(estado30D, false);

        assertThat(estadoContado.getId()).isEqualTo(estadoInicial.getId());
        assertThat(stockContado).isLessThan(stockInicial);
        assertThat(cajaContado).isGreaterThan(cajaInicial);
        assertThat(cajaContado.subtract(cajaInicial)).isGreaterThan(BigDecimal.ZERO);
        assertThat(stock30D).isLessThan(stockContado);
        assertThat(caja30D).isGreaterThan(cajaContado);
        assertThat(caja30D.subtract(cajaContado)).isGreaterThan(BigDecimal.ZERO);
//        assertThat(caja30D.subtract(cajaContado)).isEqualTo(variacionCajaContado);
        assertThat(estado60D.getStock()).isLessThan(stock30D);
        assertThat(estado60D.getCaja()).isGreaterThan(caja30D);
        assertThat(estado60D.getCaja().subtract(caja30D)).isGreaterThan(BigDecimal.ZERO);
        assertThat(estado60D.getCaja().subtract(caja30D)).isGreaterThan(variacionCaja30D);
    }
}
