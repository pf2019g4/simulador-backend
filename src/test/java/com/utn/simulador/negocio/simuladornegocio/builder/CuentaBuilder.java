package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;

public class CuentaBuilder extends AbstractPersistenceBuilder<Cuenta> {

    private CuentaBuilder() {
        instance = new Cuenta();
    }

    public static CuentaBuilder deProyecto(Proyecto proyecto, TipoCuenta tipoCuenta) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion("Caja");
        cuentaBuilder.instance.setTipoCuenta(tipoCuenta);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());
        
        return cuentaBuilder;
    }

}
