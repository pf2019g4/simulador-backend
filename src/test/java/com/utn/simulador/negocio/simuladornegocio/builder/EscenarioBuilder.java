package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;

import java.math.BigDecimal;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private EscenarioBuilder() {
        instance = new Escenario();
    }

    public static EscenarioBuilder base() {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario de test 1");
        escenarioBuilder.instance.setPeriodos(5);
        escenarioBuilder.instance.setDescripcion("descripcion de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConImpuesto(Double impuesto) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario con impuesto");
        escenarioBuilder.instance.setPeriodos(5);
        escenarioBuilder.instance.setDescripcion("descripcion de escenario con impuesto");
        escenarioBuilder.instance.setImpuestoPorcentaje(impuesto);
        return escenarioBuilder;
    }

}
