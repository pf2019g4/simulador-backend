package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import java.math.BigDecimal;

public class ModalidadPagoBuilder extends AbstractPersistenceBuilder<ModalidadPago> {

    private ModalidadPagoBuilder() {
        instance = new ModalidadPago();
    }

    public static ModalidadPagoBuilder base(Proyecto proyecto, Long porcentaje, Integer offsetPeriodo) {
        ModalidadPagoBuilder modalidadPagoBuilder = new ModalidadPagoBuilder();
        modalidadPagoBuilder.instance.setProyectoId(proyecto.getId());
        modalidadPagoBuilder.instance.setPorcentaje(BigDecimal.valueOf(porcentaje));
        modalidadPagoBuilder.instance.setOffsetPeriodo(offsetPeriodo);

        return modalidadPagoBuilder;
    }

}
