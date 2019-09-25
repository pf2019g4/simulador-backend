package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ModalidadPagoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProveedorBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.jdbc.JdbcTestUtils;

public class SimuladorProduccionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    SimuladorProduccionService produccionService;

    @Test
    public void simular_produccionValida_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);
        Long stockInicial = estadoInicial.getStock();
        BigDecimal cajaInicial = estadoInicial.getCaja();

        int cantidadCuentasAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        Estado estado = produccionService.simular(estadoInicial);

        int cantidadCuentasDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosDespues = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        assertThat(cantidadCuentasDespues).isEqualTo(cantidadCuentasAntes + 2);
        assertThat(cantidadCuentasPeriodosDespues).isEqualTo(cantidadCuentasPeriodosAntes + 2);
        assertThat(estado.getId()).isEqualTo(estadoInicial.getId());
        assertThat(estado.getStock()).isGreaterThan(stockInicial);
        assertThat(estado.getCaja()).isLessThan(cajaInicial);

    }

    @Test
    public void simular_produccionValida_Diferido_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        
        List<ModalidadPago> modalidadesPago = new ArrayList<>();
        modalidadesPago.add(ModalidadPagoBuilder.base(60L, 0).build(em)); //60% contado
        modalidadesPago.add(ModalidadPagoBuilder.base(0L, 1).build(em)); //0% a 30 dias
        modalidadesPago.add(ModalidadPagoBuilder.base(40L, 2).build(em)); //40% a 60 dias
        
        Proveedor proveedor = ProveedorBuilder.base(3.5D, 5).build(em);
        proveedor.setModalidadPago(modalidadesPago);
        
        proyecto.setProveedorSeleccionado(proveedor);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);
        Long stockInicial = estadoInicial.getStock();
        BigDecimal cajaInicial = estadoInicial.getCaja();

        int cantidadCuentasAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta");
        int cantidadCuentasPeriodosAntes = JdbcTestUtils.countRowsInTable(jdbcTemplate, "cuenta_periodo");

        estadoInicial.setPeriodo(estadoInicial.getPeriodo() + 1);
        Estado estadoContado = produccionService.simular(estadoInicial);
        Long stockContado = estadoContado.getStock();
        BigDecimal cajaContado = estadoContado.getCaja();
        BigDecimal variacionCajaContado = cajaContado.subtract(cajaInicial).abs();
        
        estadoContado.setPeriodo(estadoContado.getPeriodo() + 1);
        
        Estado estado30D = produccionService.simular(estadoInicial);
        Long stock30D = estado30D.getStock();
        BigDecimal caja30D = estado30D.getCaja();
        BigDecimal variacionCaja30D = caja30D.subtract(cajaContado).abs();

        estado30D.setPeriodo(estado30D.getPeriodo() + 1);
        Estado estado60D = produccionService.simular(estado30D);

        assertThat(estadoContado.getId()).isEqualTo(estadoInicial.getId());
        assertThat(stockContado).isGreaterThan(stockInicial);
        assertThat(cajaContado).isLessThan(cajaInicial);
        assertThat(stock30D).isGreaterThan(stockContado);
        assertThat(caja30D).isLessThan(cajaContado);
        assertThat(caja30D.subtract(cajaContado).abs()).isEqualTo(variacionCajaContado);
        assertThat(estado60D.getStock()).isGreaterThan(stock30D);
        assertThat(estado60D.getCaja()).isLessThan(caja30D);
        assertThat(estado60D.getCaja().subtract(caja30D).abs()).isEqualTo(variacionCaja30D);
    }

}
