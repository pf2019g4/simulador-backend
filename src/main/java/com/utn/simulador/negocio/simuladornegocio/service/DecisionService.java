package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final OpcionRepository opcionRepository;
    private final EstadoRepository estadoRepository;
    private final CuentaService cuentaService;

    public List<DecisionVo> obtenerPorProyecto(Long proyectoId) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));

        List<DecisionVo> decisionesVo = obtenerDecisionesPorProyecto(proyecto);

        return decisionesVo;
    }

    private List<DecisionVo> obtenerDecisionesPorProyecto(Proyecto proyecto) {
        List<Decision> decisionesPosibles = decisionRepository.findByEscenarioId(proyecto.getEscenario().getId());
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

    public void tomaDecision(Long proyectoId, Long opcionId) {
        final Opcion opcionTomada = opcionRepository.findById(opcionId).orElseThrow(() -> new IllegalArgumentException("Opcion inexistente"));
        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));

        Assert.isTrue(decisionRepository.findById(opcionTomada.getDecisionId()).get().getEscenarioId()
                .equals(proyecto.getEscenario().getId()), "La opcion no pertenece al escenario del proyecto.");
        Estado estadoActual = estadoRepository.findByProyectoIdAndActivoTrue(proyecto.getId());

        validarDecisionPendiente(proyecto, opcionTomada);

        marcarOpcionComoTomada(proyectoId, opcionTomada);
        imputarCuentasPorConsecuencia(opcionTomada, proyecto, estadoActual);
        aplicarCambiosAtributos(opcionTomada, estadoActual);
    }
    
    public Decision crearDecision(Decision decision) {
        
        return decisionRepository.save(decision);
    }
    
    public Decision editarDecision(Long decisionId, Decision decision) {
                
        opcionRepository.deleteByDecisionId(decisionId);
        
        return decisionRepository.save(decision);
    }
    
    public void borrarDecision(Long decisionId) {
        
        decisionRepository.deleteById(decisionId);
    }

    private void validarDecisionPendiente(Proyecto proyecto, final Opcion opcionTomada) throws IllegalStateException {
        for (DecisionVo decision : obtenerDecisionesPorProyecto(proyecto)) {
            if (opcionTomada.getDecisionId().equals(decision.getId())) {
                if (decision.getOpcionTomada() == null) {
                    break;
                } else {
                    throw new IllegalStateException("La decision ya fue tomada.");
                }
            }
        }
    }

    private void imputarCuentasPorConsecuencia(final Opcion opcionTomada, Proyecto proyecto, Estado estadoActual) {

        List<Cuenta> cuentasAImputar = opcionTomada.obtenerCuentasAImputar(proyecto);

        cuentaService.imputar(cuentasAImputar, estadoActual);
    }

    private void marcarOpcionComoTomada(Long proyectoId, final Opcion opcionTomada) {
        OpcionProyecto opcionProyecto = new OpcionProyecto();
        opcionProyecto.setProyectoId(proyectoId);
        opcionProyecto.setOpcion(opcionTomada);

        opcionProyectoRepository.save(opcionProyecto);
    }

    private void aplicarCambiosAtributos(Opcion opcionTomada, Estado estadoActual) {
        estadoActual.setCostoFijo(estadoActual.getCostoFijo().add(opcionTomada.getVariacionCostoFijo()));
        estadoActual.setCostoVariable(estadoActual.getCostoVariable().add(opcionTomada.getVariacionCostoVariable()));
        estadoActual.setProduccionMensual(estadoActual.getProduccionMensual() + opcionTomada.getVariacionProduccion());

        this.estadoRepository.save(estadoActual);
    }

}
