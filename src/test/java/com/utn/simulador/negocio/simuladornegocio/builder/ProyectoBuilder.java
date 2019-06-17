package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;

public class ProyectoBuilder extends AbstractPersistenceBuilder<Proyecto> {

    private ProyectoBuilder() {
        instance = new Proyecto();
    }

    public static ProyectoBuilder proyectoAbierto() {
        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();
        proyectoBuilder.instance.setNombre("Proyecto Abierto");

        return proyectoBuilder;
    }

}
