package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;

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

}
