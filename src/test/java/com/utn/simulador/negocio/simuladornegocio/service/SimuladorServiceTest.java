package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.EstadoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimuladorServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    SimuladorService simuladorService;

    @Test
    public void simularPeriodo_escenarioValido_avanzaElPeriodo() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Producto producto = ProductoBuilder.base().build(em);
        Estado estadoInicial = EstadoBuilder.inicial(producto, proyecto).build(em);

        Estado nuevoEstado = simuladorService.simularPeriodo(proyecto.getId());

        assertThat(nuevoEstado.getMes()).isEqualTo(estadoInicial.getMes() + 1);
        assertThat(nuevoEstado.getActivo()).isTrue();

    }
}
