package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.util.stream.IntStream;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;
    private final EstadoRepository estadoRepository;
    private final ProyectoRepository proyectoRepository;

    public Estado simularPeriodo(long proyectoId, boolean esForecast) {
        Estado estado = avanzarTiempo(proyectoId, esForecast);
        
        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);

        estadoService.guardar(estado);
        return estado;
    }
    
    public void crearPrimerEstadoSimulacion(Long proyectoId) {
        
        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Estado estado = proyecto.getEscenario().getEstadoInicial();
        
        estado.setId(null); //Esto me crea una copia del estado inicial del escenario
        estado.setProyecto(proyecto);
        
        estadoService.guardar(estado);
    }

    private Estado avanzarTiempo(long proyectoId, boolean esForecast) {
        Estado estado = estadoService.obtenerActual(proyectoId, esForecast);
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        return nuevoEstado;
    }

    public void simularPeriodos(Long proyectoId, boolean esForecast) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Integer maximosPeriodos = proyecto.getEscenario().getMaximosPeriodos();
        IntStream.rangeClosed(1, maximosPeriodos)
                .forEach(i -> simularPeriodo(proyectoId, esForecast));
    }

}
