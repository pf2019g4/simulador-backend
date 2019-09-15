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
        OpcionBuilder decisionBuilder = new OpcionBuilder();
        decisionBuilder.instance.setDescripcion("opcion1");
        decisionBuilder.instance.setDecision(decision);
        decisionBuilder.instance.setVariacionCostoFijo(BigDecimal.TEN.negate());
        decisionBuilder.instance.setVariacionCostoVariable(BigDecimal.TEN.negate());
        decisionBuilder.instance.setVariacionProduccion(10L);

        return decisionBuilder;
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
