package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Getter;

public enum TipoTransaccion {

    COMPRA("costo produccion"),
    VENTA("ventas");

    @Getter
    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

}
