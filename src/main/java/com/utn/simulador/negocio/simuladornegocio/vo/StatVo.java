package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatVo {

    private Producto producto;

}
