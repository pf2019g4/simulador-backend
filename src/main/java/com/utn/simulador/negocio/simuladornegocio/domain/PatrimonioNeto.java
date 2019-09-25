package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatrimonioNeto {

    private BigDecimal capitalSocial;

}
