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
        estadoBuilder.instance.setPeriodo(0);
        estadoBuilder.instance.setParametrosVentas(ParametrosVentas.builder().media(100L).desvioEstandar(new BigDecimal("10.5")).build());
        return estadoBuilder;
    }

    public static EstadoBuilder inicialConPeriodoActual(Producto producto, Proyecto proyecto, Integer periodoActual) {
        EstadoBuilder estadoBuilder = new EstadoBuilder();
        estadoBuilder.instance.setProyecto(proyecto);
        estadoBuilder.instance.setProducto(producto);
        estadoBuilder.instance.setCostoFijo(new BigDecimal("1555.5"));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(50));
        estadoBuilder.instance.setCaja(new BigDecimal(11000));
        estadoBuilder.instance.setStock(200L);
        estadoBuilder.instance.setProduccionMensual(200L);
        estadoBuilder.instance.setActivo(true);
        estadoBuilder.instance.setPeriodo(periodoActual);
        estadoBuilder.instance.setParametrosVentas(ParametrosVentas.builder().media(100L).desvioEstandar(new BigDecimal("10.5")).build());
        return estadoBuilder;
    }

    public static EstadoBuilder inicialConPeriodoYEstado(Producto producto, Proyecto proyecto, Integer periodoActual, Boolean activo) {
        EstadoBuilder estadoBuilder = new EstadoBuilder();
        estadoBuilder.instance.setProyecto(proyecto);
        estadoBuilder.instance.setProducto(producto);
        estadoBuilder.instance.setCostoFijo(new BigDecimal("1555.5"));
        estadoBuilder.instance.setCostoVariable(new BigDecimal(50));
        estadoBuilder.instance.setCaja(new BigDecimal(11000));
        estadoBuilder.instance.setStock(200L);
        estadoBuilder.instance.setProduccionMensual(200L);
        estadoBuilder.instance.setActivo(activo);
        estadoBuilder.instance.setPeriodo(periodoActual);
        estadoBuilder.instance.setParametrosVentas(ParametrosVentas.builder().media(100L).desvioEstandar(new BigDecimal("10.5")).build());
        return estadoBuilder;
    }
}
