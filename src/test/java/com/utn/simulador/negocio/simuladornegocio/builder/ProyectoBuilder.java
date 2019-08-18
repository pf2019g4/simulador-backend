package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
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

    public static ProyectoBuilder proyectoConEscenario(Escenario escenario) {

        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();
        proyectoBuilder.instance.setNombre("Proyecto con Escenario");
        proyectoBuilder.instance.setEscenario(escenario);

        return proyectoBuilder;
    }


    public Proyecto build(EntityManager em) {
        if (instance.getEscenario() == null) {
            Escenario escenario = EscenarioBuilder.base().build(em);
            this.instance.setEscenario(escenario);
        }
        Proyecto proyecto = super.build(em);
        ModalidadCobroBuilder.unPago(proyecto).build(em);
        return proyecto;
    }

}
