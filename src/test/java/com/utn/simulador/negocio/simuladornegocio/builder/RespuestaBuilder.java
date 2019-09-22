package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

public class RespuestaBuilder extends AbstractPersistenceBuilder<Opcion> {

    private RespuestaBuilder() {
        instance = new Opcion();
    }

    public static RespuestaBuilder deDecision(Decision decision) {
        RespuestaBuilder respuestaBuilder = new RespuestaBuilder();
        respuestaBuilder.instance.setDecision(decision);
        respuestaBuilder.instance.setDescripcion("DESC");

        return respuestaBuilder;
    }

}
