package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final ProyectoRepository proyectoRepository;

    public void guardar(Decision decision) {
        decisionRepository.save(decision);
    }

    public List<DecisionVo> obtenerPorProyecto(Long proyectoId) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        List<Decision> decisionesPosibles = decisionRepository.findByEscenarioId(proyecto.getEscenarioId());
        List<OpcionProyecto> opcionesTomadas = opcionProyectoRepository.findByProyectoId(proyecto.getId());

        List<DecisionVo> decisionesVo = new ArrayList<>();

        for (Decision decision : decisionesPosibles) {
            Long opcionTomadaId = null;

            for (OpcionProyecto opcionTomadaAux : opcionesTomadas) {
                if (opcionTomadaAux.getOpcion().getDecisionId().equals(decision.getId())) {
                    opcionTomadaId = opcionTomadaAux.getOpcion().getId();
                    break;
                }
            }
            decisionesVo.add(new DecisionVo(decision, opcionTomadaId));

        }

        return decisionesVo;
    }

}
