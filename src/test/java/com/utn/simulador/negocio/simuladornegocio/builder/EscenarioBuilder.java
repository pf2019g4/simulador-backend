package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;

import java.math.BigDecimal;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private EscenarioBuilder() {
        instance = new Escenario();
    }

    public static EscenarioBuilder base() {
        EscenarioBuilder modalidadCobroBuilder = new EscenarioBuilder();
        modalidadCobroBuilder.instance.setDescripcion("escenario de test 1");
        modalidadCobroBuilder.instance.setImpuestoPorcentaje(0.0);
        return modalidadCobroBuilder;
    }

    public static EscenarioBuilder escenarioConImpuesto(Double impuesto) {
        EscenarioBuilder modalidadCobroBuilder = new EscenarioBuilder();
        modalidadCobroBuilder.instance.setDescripcion("escenario con impuesto");
        modalidadCobroBuilder.instance.setImpuestoPorcentaje(impuesto);
        return modalidadCobroBuilder;
    }

}
