package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EstadoInicial {
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private Long stock;
    private Long produccionMensual;
    private Integer calidad;

}
