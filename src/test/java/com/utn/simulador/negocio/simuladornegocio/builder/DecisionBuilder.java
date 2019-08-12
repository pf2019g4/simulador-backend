package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

public class DecisionBuilder extends AbstractPersistenceBuilder<Decision> {

    private DecisionBuilder() {
        instance = new Decision();
    }

    public static DecisionBuilder deEscenario(Escenario escenario) {
        DecisionBuilder decisionBuilder = new DecisionBuilder();
        decisionBuilder.instance.setDescripcion("Se le presenta una oportunidad de financiamiento, que hace?");
        decisionBuilder.instance.setEscenarioId(escenario.getId());

        return decisionBuilder;
    }

}
