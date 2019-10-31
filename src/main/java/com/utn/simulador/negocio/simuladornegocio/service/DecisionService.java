package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.ConsecuenciaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
@RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final OpcionRepository opcionRepository;
    private final ConsecuenciaRepository consecuenciaRepository;
    private final EstadoRepository estadoRepository;
    private final CuentaService cuentaService;

    public List<DecisionVo> obtenerPorProyecto(Long proyectoId) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));

        List<DecisionVo> decisionesVo = obtenerDecisionesPorProyecto(proyecto);

        return decisionesVo;
    }

    public List<Decision> obtenerPorEscenario(Long escenarioId) {
        return decisionRepository.findByEscenarioId(escenarioId);
    }

    private List<DecisionVo> obtenerDecisionesPorProyecto(Proyecto proyecto) {
        List<Decision> decisionesPosibles = decisionRepository.findByEscenarioId(proyecto.getEscenario().getId());
        List<OpcionProyecto> opcionesTomadas = opcionProyectoRepository.findByProyectoId(proyecto.getId());
        List<DecisionVo> decisionesVo = new ArrayList<>();
        for (Decision decision : decisionesPosibles) {
            Long opcionTomadaId = null;
            for (OpcionProyecto opcionTomadaAux : opcionesTomadas) {
                if (opcionTomadaAux.getOpcion().getDecision().getId().equals(decision.getId())) {
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

        Assert.isTrue(decisionRepository.findById(opcionTomada.getDecision().getId()).get().getEscenarioId()
                .equals(proyecto.getEscenario().getId()), "La opcion no pertenece al escenario del proyecto.");
        Estado estadoActual = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(proyecto.getId(), true);

        marcarOpcionComoTomada(proyecto, opcionTomada);
        crearCuentasPorConsecuencia(opcionTomada, proyecto);
        aplicarCambiosAtributos(opcionTomada, estadoActual);
    }

    public Decision crearDecision(Decision decision) {

        //TODO: Esta es la forma correcta de persistir?
        for (Opcion opcion : decision.getOpciones()) {
            opcion.setDecision(decision);
            if (opcion.getConsecuencias() != null) {
                for (Consecuencia consecuencia : opcion.getConsecuencias()) {
                    consecuencia.setOpcion(opcion);
                }
            }
        }

        return decisionRepository.save(decision);
    }

    public Decision editarDecision(Long decisionId, Decision decision) {

        List<Long> idOpciones = new ArrayList<>();
        List<Long> idConsecuencias = new ArrayList<>();
        for (Opcion opcion : decision.getOpciones()) {
            idOpciones.add(opcion.getId());
            if (opcion.getConsecuencias() != null && !opcion.getConsecuencias().isEmpty()) {
                idConsecuencias.addAll(opcion.getConsecuencias().stream().map(c -> c.getId()).collect(Collectors.toList()));
            }
        }

        List<Opcion> opcionesBD = opcionRepository.getByDecisionId(decisionId);
        for (Opcion opcion : opcionesBD) {
            List<Consecuencia> consecuenciasBD = consecuenciaRepository.getByOpcionId(opcion.getId());
            for (Consecuencia consecuencia : consecuenciasBD) {
                if (!idConsecuencias.contains(consecuencia.getId())) {
                    consecuenciaRepository.deleteById(consecuencia.getId());
                }
            }
            if (!idOpciones.contains(opcion.getId())) {
                opcionRepository.deleteById(opcion.getId());
            }
        }

        return crearDecision(decision);
    }

    public void borrarDecision(Long decisionId) {

        decisionRepository.deleteById(decisionId);
    }

    private void crearCuentasPorConsecuencia(final Opcion opcionTomada, Proyecto proyecto) {
        List<Cuenta> cuentasACrear = opcionTomada.obtenerCuentasACrear(proyecto);
        for (Cuenta cuenta : cuentasACrear) {
            cuenta.setTipoTransaccion(TipoTransaccion.OTROS);
            cuentaService.guardar(cuenta);
        }
    }

    private void marcarOpcionComoTomada(Proyecto proyecto, final Opcion opcionTomada) {

        for (DecisionVo decision : obtenerDecisionesPorProyecto(proyecto)) {
            if (opcionTomada.getDecision().getId().equals(decision.getId())) {
                if (decision.getOpcionTomada() == null) {
                    break;
                } else {
                    opcionProyectoRepository.deleteByOpcionId(decision.getOpcionTomada());
                    break;
                }
            }
        }

        OpcionProyecto opcionProyecto = new OpcionProyecto();
        opcionProyecto.setProyectoId(proyecto.getId());
        opcionProyecto.setOpcion(opcionTomada);

        opcionProyectoRepository.save(opcionProyecto);
    }

    private void aplicarCambiosAtributos(Opcion opcionTomada, Estado estadoActual) {
        estadoActual.setCostoFijo(estadoActual.getCostoFijo().add(opcionTomada.getVariacionCostoFijo()));
        estadoActual.setCostoVariable(estadoActual.getCostoVariable().add(opcionTomada.getVariacionCostoVariable()));
        estadoActual.setProduccionMensual(estadoActual.getProduccionMensual() + opcionTomada.getVariacionProduccion());
        estadoActual.setCalidad(estadoActual.getCalidad() + opcionTomada.getVariacionCalidad());
        estadoActual.setCantidadVendedores(estadoActual.getCantidadVendedores() + opcionTomada.getVariacionCantidadVendedores());
        estadoActual.setPublicidad(estadoActual.getPublicidad() + opcionTomada.getVariacionPublicidad());

        this.estadoRepository.save(estadoActual);
    }

        }
