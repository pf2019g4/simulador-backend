package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class Activo {

    private BigDecimal caja;
    private BigDecimal cuentasPorCobrar;
    private Integer cuentasPorCobrarPeriodos;
    private BigDecimal inventario;
    private BigDecimal maquinaria;
    private BigDecimal amortizacionAcumulada;

}
