package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;

import java.math.BigDecimal;

public class EstadoBuilder extends AbstractPersistenceBuilder<Estado> {

    private EstadoBuilder() {
        instance = new Estado();
    }

    public static EstadoBuilder inicial(Proyecto proyecto) {
        EstadoBuilder estadoBuilder = new EstadoBuilder();
        estadoBuilder.instance.setProyecto(proyecto);
        estadoBuilder.instance.setCostoFijo(new BigDecimal("1555.5"));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(50));
        estadoBuilder.instance.setCaja(new BigDecimal(11000));
        estadoBuilder.instance.setStock(200L);
        estadoBuilder.instance.setProduccionMensual(200L);
        estadoBuilder.instance.setCalidad(0);
        estadoBuilder.instance.setPublicidad(new BigDecimal(500));
        estadoBuilder.instance.setCantidadVendedores(0);
        estadoBuilder.instance.setActivo(true);
        estadoBuilder.instance.setPeriodo(0);
        estadoBuilder.instance.setEsForecast(true);
        return estadoBuilder;
    }

    public static EstadoBuilder inicialConPeriodoActual(Proyecto proyecto, Integer periodoActual) {
        EstadoBuilder estadoBuilder = inicial(proyecto);
        estadoBuilder.instance.setPeriodo(periodoActual);
        return estadoBuilder;
    }

    public static EstadoBuilder inicialConPeriodoYEstado(Proyecto proyecto, Integer periodoActual, Boolean activo) {
        EstadoBuilder estadoBuilder = inicial(proyecto);
        estadoBuilder.instance.setActivo(activo);
        estadoBuilder.instance.setPeriodo(periodoActual);
        estadoBuilder.instance.setEsForecast(true);
        return estadoBuilder;
    }
}
