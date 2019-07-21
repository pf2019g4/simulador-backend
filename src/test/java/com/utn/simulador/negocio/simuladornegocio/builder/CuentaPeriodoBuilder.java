package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import java.math.BigDecimal;

public class CuentaPeriodoBuilder extends AbstractPersistenceBuilder<CuentaPeriodo> {

    private CuentaPeriodoBuilder() {
        instance = new CuentaPeriodo();
    }

    public static CuentaPeriodoBuilder deCuenta(Cuenta cuenta, int periodo) {
        CuentaPeriodoBuilder cuentaPeriodoBuilder = new CuentaPeriodoBuilder();

        cuentaPeriodoBuilder.instance = CuentaPeriodo.builder()
                .monto(new BigDecimal("500.50"))
                .cuenta(cuenta)
                .periodo(periodo).build();

        return cuentaPeriodoBuilder;

    }

}
