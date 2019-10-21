package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.EstadoInicial;

import java.math.BigDecimal;

public class EstadoInicialBuilder extends AbstractPersistenceBuilder<EstadoInicial> {

    private EstadoInicialBuilder() {
        instance = new EstadoInicial();
    }

    public static EstadoInicialBuilder baseParaEscenario() {
        EstadoInicialBuilder estadoBuilder = new EstadoInicialBuilder();
        estadoBuilder.instance.setCostoFijo(new BigDecimal("1555.5"));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(50));
        estadoBuilder.instance.setStock(200L);
        estadoBuilder.instance.setProduccionMensual(200L);
        estadoBuilder.instance.setCalidad(5);
        return estadoBuilder;
    }
}
