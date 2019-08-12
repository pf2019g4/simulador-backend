package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Consecuencia;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;

import java.math.BigDecimal;

public class ConsecuenciaBuilder extends AbstractPersistenceBuilder<Consecuencia> {

    private ConsecuenciaBuilder() {
        instance = new Consecuencia();
    }

    public static ConsecuenciaBuilder financieraEgresoNoImpuestoParaOpcion(Opcion opcion) {
        ConsecuenciaBuilder consecuenciaBuilder = new ConsecuenciaBuilder();
        consecuenciaBuilder.instance.setMonto(new BigDecimal("300"));
        consecuenciaBuilder.instance.setOpcionId(opcion.getId());
        consecuenciaBuilder.instance.setTipoCuenta(TipoCuenta.FINANCIERO);
        consecuenciaBuilder.instance.setTipoFlujoFondo(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS);
        consecuenciaBuilder.instance.setDescripcion("opcion financiera no afecta a impuestos");

        return consecuenciaBuilder;
    }

}
