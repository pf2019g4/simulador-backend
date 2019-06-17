package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.ProductoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductoServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    ProductoService productoService;

    @Test
    public void obtenerPorProyecto_existente_producto() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        ProductoBuilder.deProyecto(proyecto).build(em);

        Producto productoObtenido = productoService.obtenerProducto();

        assertThat(productoObtenido).isNotNull();
    }
}
