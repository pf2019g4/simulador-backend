package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import java.math.BigDecimal;

public class ModalidadCobroBuilder extends AbstractPersistenceBuilder<ModalidadCobro> {

    private ModalidadCobroBuilder() {
        instance = new ModalidadCobro();
    }

    public static ModalidadCobroBuilder base(Proyecto proyecto, Long porcentaje, Integer offsetPeriodo) {
        ModalidadCobroBuilder modalidadCobroBuilder = new ModalidadCobroBuilder();
        modalidadCobroBuilder.instance.setProyecto(proyecto);
        modalidadCobroBuilder.instance.setPorcentaje(BigDecimal.valueOf(porcentaje));
        modalidadCobroBuilder.instance.setOffsetPeriodo(offsetPeriodo);

        return modalidadCobroBuilder;
    }

}
