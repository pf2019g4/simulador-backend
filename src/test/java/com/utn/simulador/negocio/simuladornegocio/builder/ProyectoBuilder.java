package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class ProyectoBuilder extends AbstractPersistenceBuilder<Proyecto> {

    private ProyectoBuilder() {
        instance = new Proyecto();
    }

    public static ProyectoBuilder proyectoAbierto() {

        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();
        proyectoBuilder.instance.setNombre("Proyecto Abierto");
        proyectoBuilder.instance.setImpuestoPorcentaje(0.0d);

        return proyectoBuilder;
    }

    public Proyecto build(EntityManager em) {
        Escenario escenario = EscenarioBuilder.base().build(em);

        this.instance.setEscenarioId(escenario.getId());
        Proyecto proyecto = super.build(em);
        
        List<ModalidadCobro> modalidadesCobro = new ArrayList<>();
        ModalidadCobro modalidadCobro = ModalidadCobroBuilder.base(proyecto, 100L, 0).build(em); //Crea modalidad de cobro basica (Contado)
        modalidadesCobro.add(modalidadCobro);
        this.instance.setModalidadCobro(modalidadesCobro);
        return proyecto;
    }

}
