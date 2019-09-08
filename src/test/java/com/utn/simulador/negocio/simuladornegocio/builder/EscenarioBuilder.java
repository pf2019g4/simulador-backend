package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import org.xmlunit.diff.ElementSelector;

import java.math.BigDecimal;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private EscenarioBuilder() {
        instance = new Escenario();
    }

    public static EscenarioBuilder base() {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(12);
        return escenarioBuilder;
    }

    public static EscenarioBuilder baseConEscenario(Estado estado) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(12);
        escenarioBuilder.instance.setEstadoInicial(estado);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConImpuesto(Double impuesto) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario con impuesto");
        escenarioBuilder.instance.setImpuestoPorcentaje(impuesto);
        escenarioBuilder.instance.setMaximosPeriodos(12);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConCantidadMaximaPeriodos(Integer maximosPeriodos) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario con peridos");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(maximosPeriodos);
        return escenarioBuilder;
    }
}
