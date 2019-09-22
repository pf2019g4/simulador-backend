package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.Activo;
import com.utn.simulador.negocio.simuladornegocio.domain.Pasivo;
import com.utn.simulador.negocio.simuladornegocio.domain.PatrimonioNeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceVo {

    Activo activo;
    Pasivo pasivo;
    PatrimonioNeto patrimonioNeto;

}
