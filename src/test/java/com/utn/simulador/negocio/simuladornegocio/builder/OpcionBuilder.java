package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class OpcionBuilder extends AbstractPersistenceBuilder<Opcion> {

    private List<Consecuencia> consecuencias;

    private OpcionBuilder() {
        instance = new Opcion();
        consecuencias = new ArrayList<>();
    }

    public static OpcionBuilder deDecisionMaquinaria(Decision decision) {
        OpcionBuilder opcionBuilder = new OpcionBuilder();
        opcionBuilder.instance.setDescripcion("opcion1");
        opcionBuilder.instance.setDecision(decision);
        opcionBuilder.instance.setVariacionCostoFijo(BigDecimal.TEN.negate());
        opcionBuilder.instance.setVariacionCostoVariable(BigDecimal.TEN.negate());
        opcionBuilder.instance.setVariacionProduccion(10L);
        opcionBuilder.instance.setVariacionCalidad(0);
        opcionBuilder.instance.setVariacionCantidadVendedores(0);
        opcionBuilder.instance.setVariacionPublicidad(0);

        return opcionBuilder;
    }

    public OpcionBuilder conConsecuencia(Consecuencia consecuencia) {
        this.consecuencias.add(consecuencia);
        return this;
    }

    public Opcion build(EntityManager em) {
        em.persist(instance);
        em.flush();

        instance.setConsecuencias(new ArrayList<>());

        for (Consecuencia consecuencia : consecuencias) {
            consecuencia.setOpcion(instance);
            em.persist(consecuencia);
            instance.getConsecuencias().add(consecuencia);
        }

        em.flush();
        em.detach(instance);
        return instance;
    }

}
