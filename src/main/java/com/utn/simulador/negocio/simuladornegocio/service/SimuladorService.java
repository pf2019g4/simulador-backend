package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;

import java.util.stream.IntStream;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;

    private final EstadoRepository estadoRepository;
    private final ProyectoRepository proyectoRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final CuentaService cuentaService;

    public Estado simularPeriodo(long proyectoId, boolean esForecast) {
        Estado estadoInicial = estadoService.obtenerActual(proyectoId, esForecast);
        Estado nuevoEstado = avanzarTiempo(estadoInicial, esForecast);
        simuladorProduccionService.simular(nuevoEstado);
        simuladorVentasService.simular(nuevoEstado);
        estadoService.guardar(nuevoEstado);
        return nuevoEstado;
    }

    public void crearPrimerEstadoSimulacion(Long proyectoId, boolean esForecast) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Estado estado = proyecto.getEscenario().getEstadoInicial();

        Estado estadoNuevo = estado.toBuilder().id(null)
                .activo(true)
                .esForecast(esForecast)
                .proyecto(proyecto)
                .build();

        estadoService.guardar(estadoNuevo);
    }

    private Estado avanzarTiempo(Estado estado, boolean esForecast) {
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        cuentaService.inputarCuetasNuevoPeriodo(nuevoEstado);

        estadoRepository.save(nuevoEstado);
        return nuevoEstado;
    }

    public void simularPeriodos(Long proyectoId, boolean esForecast) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Integer maximosPeriodos = proyecto.getEscenario().getMaximosPeriodos();
        IntStream.rangeClosed(1, maximosPeriodos)
                .forEach(i -> simularPeriodo(proyectoId, esForecast));
    }

    public void deshacerSimulacionPrevia(Long proyectoId) {
        for (OpcionProyecto op : opcionProyectoRepository.findByProyectoId(proyectoId)) {
            opcionProyectoRepository.deleteById(op.getId());
        }
        cuentaService.eliminarCuentasDeProyecto(proyectoId);
        estadoService.borrarEstadosForecast(proyectoId);

    }

}
