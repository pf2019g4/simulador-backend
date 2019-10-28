package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.EmpresaCompetidora;
import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.domain.RestriccionPrecio;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoEscenarioVo {

    private List<EmpresaCompetidora> empresasCompetidoras;
    private List<MercadoPeriodo> mercadosPeriodo;
    private List<PonderacionMercado> ponderacionesMercado;
    private RestriccionPrecio restriccionPrecio;
}
