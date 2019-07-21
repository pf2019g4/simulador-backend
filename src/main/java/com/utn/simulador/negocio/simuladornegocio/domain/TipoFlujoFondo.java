package com.utn.simulador.negocio.simuladornegocio.domain;


public enum TipoFlujoFondo {

    INGRESOS_AFECTOS_A_IMPUESTOS ("Ingresos afectos a impuestos"),
    EGRESOS_AFECTOS_A_IMPUESTOS("Egresos afectos a impuestos"),
    GASTOS_NO_DESEMBOLSABLES("Gastos no desembolsables"),
    UTILIDAD_NETA_ANTES_DE_IMPUESTOS("Utilidad neta antes de impuestos"),
    IMPUESTOS("Impuestos"),
    UTILIDAD_DESPUES_DE_IMPUESTOS("Utilidad despues de impuestos"),
    AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES("Ajuste de gastos no desembolsables"),
    INGRESOS_NO_AFECTOS_A_IMPUESTOS("Ingresos no afectos a impuestos"),
    EGRESOS_NO_AFECTOS_A_IMPUESTOS("Egresos no afectos a impuestos"),
    FLUJO_DE_FONDOS("Flujo de fondos");

    private final String descripcion;

    TipoFlujoFondo(String descripcion) {
        this.descripcion = descripcion;
    }

}
