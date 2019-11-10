package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EmpresaCompetidorasBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EscenarioBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoInicialBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ForecastBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.EstadoInicial;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.RestriccionPrecio;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MercadoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private MercadoService mercadoService;

    @Test
    public void obtenerCuotaMercado_proyectoValido_calculaCuotaMercado() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(5, 5, 5).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.ZERO).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(15);

    }

    @Test
    public void obtenerCuotaMercado_soloMercadoAlto_calculaCuotaMercado() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(5, 0, 0).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.ZERO).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(5L);

    }

    @Test
    public void obtenerCuotaMercado_soloMercadoMedio_calculaCuotaMercado() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(0, 5, 0).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.ZERO).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(5L);

    }

    @Test
    public void obtenerCuotaMercado_soloMercadoBajo_calculaCuotaMercado() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial).build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(0, 0, 5).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);
        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.ZERO).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(5L);

    }

    @Test
    public void obtenerCuotaMercado_precioPorDebajoDelMinimo_0() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial)
                .conRestriccionPrecio(RestriccionPrecio.builder().precioMin(BigDecimal.ONE).precioMax(new BigDecimal(10000)).build())
                .build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(0, 0, 5).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);

        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.ZERO).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(0L);

    }

    @Test
    public void obtenerCuotaMercado_precioPorSobreMaximo_0() {

        EstadoInicial estadoInicial = EstadoInicialBuilder.baseParaEscenario().build();
        Escenario escenario = EscenarioBuilder.baseConEstadoInicial(estadoInicial)
                .conRestriccionPrecio(RestriccionPrecio.builder().precioMin(BigDecimal.ONE).precioMax(BigDecimal.ONE).build())
                .build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).conPonderacionesMercado(0, 0, 5).build(em);
        Estado estado = EstadoBuilder.inicial(proyecto).build(em);
        estado.setPeriodo(1);

        ForecastBuilder.baseDeProyectoYPeriodo(proyecto, estado.getPeriodo()).conPrecio(BigDecimal.TEN).build(em);

        EmpresaCompetidorasBuilder.todosMercadosAl95(escenario).build(em);

        long cuotaMercado = mercadoService.obtenerCuotaMercado(estado);

        Assertions.assertThat(cuotaMercado).isEqualTo(0L);

    }

}
