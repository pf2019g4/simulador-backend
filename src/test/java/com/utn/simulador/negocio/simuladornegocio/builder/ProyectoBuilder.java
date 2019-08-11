package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import javax.persistence.EntityManager;

public class ProyectoBuilder extends AbstractPersistenceBuilder<Proyecto> {

    private ProyectoBuilder() {
        instance = new Proyecto();
    }

    public static ProyectoBuilder proyectoAbierto() {
        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();
        proyectoBuilder.instance.setNombre("Proyecto Abierto");

        return proyectoBuilder;
    }

    public Proyecto build(EntityManager em) {
        Proyecto proyecto = super.build(em);
        ModalidadCobroBuilder.unPago(proyecto).build(em);
        return proyecto;
    }

}
