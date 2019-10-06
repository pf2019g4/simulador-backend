package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import java.math.BigDecimal;

public class FinanciacionBuilder extends AbstractPersistenceBuilder<Financiacion> {

    private FinanciacionBuilder() {
        instance = new Financiacion();
    }

    public static FinanciacionBuilder doceCuotas(Escenario escenario) {
        FinanciacionBuilder financiacionBuilder = new FinanciacionBuilder();
        financiacionBuilder.instance.setDescripcion("Santander");
        financiacionBuilder.instance.setCantidadCuotas(12);
        financiacionBuilder.instance.setTna(BigDecimal.ONE);
        financiacionBuilder.instance.setEscenarioId(escenario.getId());

        return financiacionBuilder;
    }

}
