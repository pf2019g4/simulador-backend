package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EscenarioBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import static org.assertj.core.api.Assertions.assertThat;

import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.RespuestaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimuladorServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    SimuladorService simuladorService;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    public void simularPeriodo_escenarioValido_avanzaElPeriodo() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);

        Estado nuevoEstado = simuladorService.simularPeriodo(proyecto.getId());

        assertThat(nuevoEstado.getPeriodo()).isEqualTo(estadoInicial.getPeriodo() + 1);
        assertThat(nuevoEstado.getActivo()).isTrue();

    }

    @Test
    public void simularPeriodos_escenarioValido_avanzaElPeriodoHastaElMaximo() {
        Escenario escenario = EscenarioBuilder.base().build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConEscenario(escenario).build(em);
        Producto producto = ProductoBuilder.base().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);

        simuladorService.simularPeriodos(proyecto.getId());

        assertThat(estadoRepository.findByProyectoIdAndActivoTrue(proyecto.getId()).getPeriodo()).isEqualTo(estadoInicial.getPeriodo() + escenario.getMaximosPeriodos());

    }
}
