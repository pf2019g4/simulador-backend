package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private EscenarioBuilder() {
        instance = new Escenario();
    }

    public static EscenarioBuilder base() {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario de test 1");
        escenarioBuilder.instance.setDescripcion("descripcion de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(5);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        return escenarioBuilder;
    }

    public static EscenarioBuilder baseConEstado(Estado estado) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario de test 1");
        escenarioBuilder.instance.setDescripcion("descripcion de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(12);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        escenarioBuilder.instance.setEstadoInicial(estado);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConImpuesto(Double impuesto) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario con impuesto");
        escenarioBuilder.instance.setDescripcion("descripcion de escenario con impuesto");
        escenarioBuilder.instance.setImpuestoPorcentaje(impuesto);
        escenarioBuilder.instance.setMaximosPeriodos(5);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConCantidadMaximaPeriodos(Integer maximosPeriodos) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario con peridos");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(maximosPeriodos);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        return escenarioBuilder;
    }
}
