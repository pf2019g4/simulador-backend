package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;


@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@MappedSuperclass
public class EstadoInicial {
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private Long stock;
    private Long produccionMensual;
    private Integer calidad;

}
