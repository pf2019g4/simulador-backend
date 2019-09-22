package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Activo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Pasivo;
import com.utn.simulador.negocio.simuladornegocio.domain.PatrimonioNeto;
import com.utn.simulador.negocio.simuladornegocio.vo.BalanceVo;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final EstadoService estadoService;

    public BalanceVo obtenerPorProyecto(Long proyectoId) {
        Estado estado = estadoService.obtenerActual(proyectoId, false);
        Activo activo = new Activo(estado.getCaja(), null, estado.getStock(), estado.getMaquinarias(), estado.getAmortizacionAcumulada());
        Pasivo pasivo = new Pasivo();
        PatrimonioNeto patrimonioNeto = new PatrimonioNeto();
        return new BalanceVo(activo, pasivo, patrimonioNeto);
    }


}
