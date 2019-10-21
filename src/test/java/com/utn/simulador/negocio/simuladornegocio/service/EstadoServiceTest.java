package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EstadoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    EstadoService estadoService;

    @Test
    public void obtenerEstadoActual_estadoExistente_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        EstadoBuilder.inicial(proyecto).build(em);

        Estado estado = estadoService.obtenerActual(proyecto.getId(),true);
        assertThat(estado).isNotNull();
    }

    @Test
    public void obtenerEstado_porProyecto() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        EstadoBuilder.inicialConPeriodoYEstado(proyecto, 0, false).build(em);
        EstadoBuilder.inicialConPeriodoYEstado(proyecto, 1, false).build(em);
        EstadoBuilder.inicialConPeriodoYEstado(proyecto, 2, true).build(em);

        List<Estado> estados = estadoService.obtenerPorProyecto(proyecto.getId());
        assertThat(estados.size()).isEqualTo(3);
    }
}
