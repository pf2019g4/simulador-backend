package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Activo;
import com.utn.simulador.negocio.simuladornegocio.domain.Balance;
import com.utn.simulador.negocio.simuladornegocio.domain.Pasivo;
import com.utn.simulador.negocio.simuladornegocio.domain.PatrimonioNeto;
import java.math.BigDecimal;

public class BalanceBuilder extends AbstractPersistenceBuilder<Balance> {

    private BalanceBuilder() {
        instance = new Balance();
    }

    public static BalanceBuilder balanceInicial() {
        BalanceBuilder balanceBuilder = new BalanceBuilder();

        balanceBuilder.instance.setPatrimonioNeto(PatrimonioNeto.builder().capitalSocial(BigDecimal.ZERO).resultadoDelEjercicio(BigDecimal.ZERO).build());
        balanceBuilder.instance.setActivo(
                Activo.builder()
                        .amortizacionAcumulada(BigDecimal.ZERO)
                        .caja(BigDecimal.TEN)
                        .maquinaria(BigDecimal.ZERO)
                        .cuentasPorCobrar(BigDecimal.TEN)
                        .cuentasPorCobrarPeriodos(1)
                        .inventario(BigDecimal.TEN)
                        .build()
        );

        balanceBuilder.instance.setPasivo(Pasivo.builder()
                .deudasBancarias(BigDecimal.ONE)
                .deudasBancariasPeriodos(1)
                .proveedores(BigDecimal.TEN)
                .proveedoresPeriodos(1)
                .build()
        );

        return balanceBuilder;
    }

}
