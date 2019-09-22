package com.utn.simulador.negocio.simuladornegocio.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceVo {
    private class Activo {
        private BigDecimal caja;
        private BigDecimal cuentasACobrar;
        private BigDecimal inventario;
        private BigDecimal maquinaria;
        private BigDecimal amortizacionAcumulada;
    }

    private class Pasivo {
        private BigDecimal proveedores;
        private BigDecimal deudasBancarias;
    }

    private class PatrimonioNeto {
        private BigDecimal capitalSocial;
    }

    Activo activo;
    Pasivo pasivo;
    PatrimonioNeto patrimonioNeto;

}
