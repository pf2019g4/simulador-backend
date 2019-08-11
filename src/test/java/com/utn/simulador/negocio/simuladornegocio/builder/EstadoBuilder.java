package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.ParametrosVentas;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;

import java.math.BigDecimal;

public class EstadoBuilder extends AbstractPersistenceBuilder<Estado> {

    private EstadoBuilder() {
        instance = new Estado();
    }

    public static EstadoBuilder inicial(Producto producto, Proyecto proyecto) {
        EstadoBuilder estadoBuilder = new EstadoBuilder();
        estadoBuilder.instance.setProducto(producto);
        estadoBuilder.instance.setProyecto(proyecto);
        estadoBuilder.instance.setCostoFijo(new BigDecimal("1555.5"));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(50));
        estadoBuilder.instance.setCaja(new BigDecimal(11000));
        estadoBuilder.instance.setStock(200L);
        estadoBuilder.instance.setProduccionMensual(200L);
        estadoBuilder.instance.setActivo(true);
        estadoBuilder.instance.setMes(0);
        estadoBuilder.instance.setParametrosVentas(ParametrosVentas.builder().media(100L).desvioEstandar(new BigDecimal("10.5")).build());
        return estadoBuilder;
    }

}
