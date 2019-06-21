package com.utn.simulador.negocio.simuladornegocio.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrosVentas {

    @Column(name = "parametros_ventas_media")
    Long media;
    @Column(name = "parametros_ventas_desvio")
    BigDecimal desvioEstandar;

}
