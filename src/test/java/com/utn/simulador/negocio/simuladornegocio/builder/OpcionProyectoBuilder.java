package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

public class OpcionProyectoBuilder extends AbstractPersistenceBuilder<OpcionProyecto> {

    private OpcionProyectoBuilder() {
        instance = new OpcionProyecto();
    }

    public static OpcionProyectoBuilder deOpcionYProyecto(Opcion opcion, Proyecto proyecto) {
        OpcionProyectoBuilder opcionProyectoBuilder = new OpcionProyectoBuilder();
        opcionProyectoBuilder.instance.setOpcion(opcion);
        opcionProyectoBuilder.instance.setProyectoId(proyecto.getId());

        return opcionProyectoBuilder;
    }

}
