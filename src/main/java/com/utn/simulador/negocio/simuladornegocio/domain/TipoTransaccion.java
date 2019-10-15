package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Getter;

public enum TipoTransaccion {

    COSTO_PRODUCCION("Costo producción"),
    VENTA("Ventas"),
    OTROS("Otros");

    @Getter
    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

}
