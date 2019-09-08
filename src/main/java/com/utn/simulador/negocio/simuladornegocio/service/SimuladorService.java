package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;
    private final EscenarioRepository escenarioRepository;
    private final ForecastService forecastService;

    public Estado simularPeriodo(long proyectoId, boolean esForecast) {
        Estado estado = avanzarTiempo(proyectoId, esForecast);

        
        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);

        estadoService.guardar(estado);
        return estado;
    }

    private Estado avanzarTiempo(long proyectoId, boolean esForecast) {
        Estado estado = estadoService.obtenerActual(proyectoId, esForecast);
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        return nuevoEstado;
    }

    public void simularPeriodos(Long proyectoId, boolean esForecast) {
        Integer maximosPeriodos = escenarioRepository.findById(proyectoId).get().getMaximosPeriodos();
        IntStream.rangeClosed(1, maximosPeriodos)
                .forEach(i -> simularPeriodo(proyectoId, esForecast));
    }

}
