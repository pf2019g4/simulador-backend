package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

public class RespuestaBuilder extends AbstractPersistenceBuilder<Respuesta> {

    private RespuestaBuilder() {
        instance = new Respuesta();
    }

    public static RespuestaBuilder deDecision(Decision decision) {
        RespuestaBuilder respuestaBuilder = new RespuestaBuilder();
        respuestaBuilder.instance.setDecisionId(decision.getId());
        respuestaBuilder.instance.setDescripcion("DESC");

        return respuestaBuilder;
    }

}
