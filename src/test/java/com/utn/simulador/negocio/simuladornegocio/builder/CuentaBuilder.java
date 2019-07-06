package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;

import java.math.BigDecimal;

public class CuentaBuilder extends AbstractPersistenceBuilder<Cuenta> {

    private CuentaBuilder() {
        instance = new Cuenta();
    }

    public static CuentaBuilder deProyecto(Proyecto proyecto, TipoCuenta tipoCuenta, Integer periodo) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion("Caja");
        cuentaBuilder.instance.setMonto(new BigDecimal("500.50"));
        cuentaBuilder.instance.setTipoCuenta(tipoCuenta);
        cuentaBuilder.instance.setPeriodo(periodo);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());

        return cuentaBuilder;
    }

}
