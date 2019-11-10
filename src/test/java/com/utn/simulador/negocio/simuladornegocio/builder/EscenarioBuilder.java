package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Balance;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.EstadoInicial;
import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import javax.persistence.EntityManager;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private EscenarioBuilder() {
        instance = new Escenario();
    }

    public static EscenarioBuilder base() {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setTitulo("escenario de test 1");
        escenarioBuilder.instance.setDescripcion("descripcion de test 1");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(5);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        return escenarioBuilder;
    }

    public static EscenarioBuilder baseConEstadoInicial(EstadoInicial estado) {
        EscenarioBuilder escenarioBuilder = base();
        escenarioBuilder.instance.setEstadoInicial(estado);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConImpuesto(Double impuesto) {
        EscenarioBuilder escenarioBuilder = base();
        escenarioBuilder.instance.setImpuestoPorcentaje(impuesto);
        return escenarioBuilder;
    }

    public static EscenarioBuilder escenarioConCantidadMaximaPeriodos(Integer maximosPeriodos) {
        EscenarioBuilder escenarioBuilder = new EscenarioBuilder();
        escenarioBuilder.instance.setDescripcion("escenario con peridos");
        escenarioBuilder.instance.setImpuestoPorcentaje(0.0);
        escenarioBuilder.instance.setMaximosPeriodos(maximosPeriodos);
        escenarioBuilder.instance.setNombrePeriodos("Mes");
        return escenarioBuilder;
    }

    public EscenarioBuilder conBalanceInicial(Balance balance) {
        this.instance.setBalanceInicial(balance);
        return this;
    }

    @Override
    public Escenario build(EntityManager em) {

        em.persist(instance);

        for (int i = 1; i <= instance.getMaximosPeriodos(); i++) {
            MercadoPeriodo mercadoPeriodo = MercadoPeriodo.builder().escenarioId(instance.getId()).periodo(i).alto(100).medio(100).bajo(100).build();
            em.persist(mercadoPeriodo);
        }

        em.flush();
        em.detach(instance);
        return instance;
    }
}
