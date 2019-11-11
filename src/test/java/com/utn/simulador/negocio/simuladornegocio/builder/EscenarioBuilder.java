package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Balance;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.EstadoInicial;
import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.RestriccionPrecio;
import java.math.BigDecimal;
import javax.persistence.EntityManager;

public class EscenarioBuilder extends AbstractPersistenceBuilder<Escenario> {

    private RestriccionPrecio restriccionPrecio;

    private EscenarioBuilder() {
        instance = new Escenario();
        restriccionPrecio = RestriccionPrecio.builder().precioMax(new BigDecimal(999999)).precioMin(BigDecimal.ZERO).build();

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

    public EscenarioBuilder conRestriccionPrecio(RestriccionPrecio restriccionPrecio) {
        this.restriccionPrecio = restriccionPrecio;
        return this;
    }

    @Override
    public Escenario build(EntityManager em) {

        em.persist(instance);

        crearMercadosPeriodos(em);

        crearLaRestriccionDePrecio(em);

        em.flush();
        em.detach(instance);
        return instance;
    }

    private void crearLaRestriccionDePrecio(EntityManager em) {
        this.restriccionPrecio.setEscenarioId(instance.getId());
        em.persist(restriccionPrecio);
    }

    private void crearMercadosPeriodos(EntityManager em) {
        for (int i = 1; i <= instance.getMaximosPeriodos(); i++) {
            MercadoPeriodo mercadoPeriodo = MercadoPeriodo.builder().escenarioId(instance.getId()).periodo(i).alto(100).medio(100).bajo(100).build();
            em.persist(mercadoPeriodo);
        }
    }
}
