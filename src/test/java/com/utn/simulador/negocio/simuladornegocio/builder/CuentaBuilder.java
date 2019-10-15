package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;

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

    public static CuentaBuilder deProyectoConDescripcionYTipoTransaccion(Proyecto proyecto, String descripcion, TipoFlujoFondo tipoFlujoFondo, TipoTransaccion tipoTransaccion) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion(descripcion);
        cuentaBuilder.instance.setTipoFlujoFondo(tipoFlujoFondo);
        cuentaBuilder.instance.setTipoCuenta(TipoCuenta.FINANCIERO);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());
        cuentaBuilder.instance.setTipoTransaccion(tipoTransaccion);
        cuentaBuilder.instance.setCuentasPeriodo(new ArrayList<>());

        return cuentaBuilder;
    }
    public static CuentaBuilder deProyectoTipoEconomico(Proyecto proyecto, String descripcion, TipoTransaccion tipoTransaccion) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion(descripcion);
        cuentaBuilder.instance.setTipoCuenta(TipoCuenta.ECONOMICO);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());
        cuentaBuilder.instance.setCuentasPeriodo(new ArrayList<>());
        cuentaBuilder.instance.setTipoTransaccion(tipoTransaccion);

        return cuentaBuilder;
    }
}
