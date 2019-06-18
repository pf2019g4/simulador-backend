package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;

import java.math.BigDecimal;

public class EstadoBuilder extends AbstractPersistenceBuilder<Estado> {


    private EstadoBuilder() {
        instance = new Estado();
    }

    public static EstadoBuilder inicial(Producto producto) {
        EstadoBuilder estadoBuilder = new EstadoBuilder();
        estadoBuilder.instance.setProducto(producto);
        estadoBuilder.instance.setCostoFijo(new BigDecimal(0));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(0));
        estadoBuilder.instance.setCaja(new BigDecimal(0));
        estadoBuilder.instance.setStock(0L);
        estadoBuilder.instance.setProduccionMensual(0L);
        estadoBuilder.instance.setActivo(true);
        estadoBuilder.instance.setMes(0);
        return estadoBuilder;
    }

}

