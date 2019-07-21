package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CuentaBuilder extends AbstractPersistenceBuilder<Cuenta> {

    private CuentaBuilder() {
        instance = new Cuenta();
        instance.setCuentasPeriodo(new ArrayList<CuentaPeriodo>());
    }

    public static CuentaBuilder deProyecto(Proyecto proyecto, TipoCuenta tipoCuenta, Integer periodo) {
        CuentaBuilder cuentaBuilder = new CuentaBuilder();
        cuentaBuilder.instance.setDescripcion("Caja");
        cuentaBuilder.instance.setTipoCuenta(tipoCuenta);
        cuentaBuilder.instance.setProyectoId(proyecto.getId());

        CuentaPeriodo cuentaPeriodo = CuentaPeriodo.builder()
                .monto(new BigDecimal("500.50"))
                .periodo(periodo)
                .build();

        cuentaBuilder.instance.getCuentasPeriodo().add(cuentaPeriodo);

        return cuentaBuilder;
    }

}
