package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import java.util.List;

public class DecisionBuilder extends AbstractPersistenceBuilder<Decision> {

    private DecisionBuilder() {
        instance = new Decision();
    }

    public static DecisionBuilder deProyecto(Proyecto proyecto) {
        DecisionBuilder decisionBuilder = new DecisionBuilder();
        decisionBuilder.instance.setDescripcion("Se le presenta una oportunidad de financiamiento, que hace?");
        decisionBuilder.instance.setProyectoId(proyecto.getId());

        return decisionBuilder;
    }

}
