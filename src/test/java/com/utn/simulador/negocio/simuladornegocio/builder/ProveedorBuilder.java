package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import java.math.BigDecimal;

public class ProveedorBuilder extends AbstractPersistenceBuilder<Proveedor> {

    private ProveedorBuilder() {
        instance = new Proveedor();
    }

    public static ProveedorBuilder base(Double costoVariable, Integer calidad) {
        ProveedorBuilder proveedorBuilder = new ProveedorBuilder();
        proveedorBuilder.instance.setNombre("Proveedor Base");
        proveedorBuilder.instance.setVariacionCostoVariable(BigDecimal.valueOf(costoVariable));
        proveedorBuilder.instance.setVariacionCalidad(calidad);

        return proveedorBuilder;
    }

}
