package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EstadoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    EstadoService estadoService;

    @Test
    public void obtenerEstadoActual_estadoExistente_estado() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        EstadoBuilder.inicial(producto, proyecto).build(em);

        Estado estado = estadoService.obtenerActual();
        assertThat(estado).isNotNull();
    }
}
