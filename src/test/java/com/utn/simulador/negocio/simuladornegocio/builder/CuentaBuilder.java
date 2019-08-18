package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;

import java.util.ArrayList;

public class CuentaBuilder extends AbstractPersistenceBuilder<Cuenta> {

    private CuentaBuilder() {
        instance = new Cuenta();
    }

    public static CuentaBuilder deProyecto(Proyecto proyecto, TipoFlujoFondo tipoFlujoFondo) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion("Caja");
        cuentaBuilder.instance.setTipoFlujoFondo(tipoFlujoFondo);
        cuentaBuilder.instance.setTipoCuenta(TipoCuenta.FINANCIERO);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());
        cuentaBuilder.instance.setCuentasPeriodo(new ArrayList<>());

        return cuentaBuilder;
    }

    public static CuentaBuilder deProyectoConDescripcion(Proyecto proyecto, String descripcion, TipoFlujoFondo tipoFlujoFondo) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion(descripcion);
        cuentaBuilder.instance.setTipoFlujoFondo(tipoFlujoFondo);
        cuentaBuilder.instance.setTipoCuenta(TipoCuenta.FINANCIERO);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());
        cuentaBuilder.instance.setCuentasPeriodo(new ArrayList<>());

        return cuentaBuilder;
    }

}
