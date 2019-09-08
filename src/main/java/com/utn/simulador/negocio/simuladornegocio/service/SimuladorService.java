package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;
    private final EscenarioRepository escenarioRepository;

    public Estado simularPeriodo(long proyectoId) {
        Estado estado = avanzarTiempo();

        
        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);

        estadoService.guardar(estado);
        return estado;
    }

    private Estado avanzarTiempo() {
        Estado estado = estadoService.obtenerActual();
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        return nuevoEstado;
    }

    public void simularPeriodos(Long proyectoId) {
        Integer maximosPeriodos = escenarioRepository.findById(proyectoId).get().getMaximosPeriodos();
        IntStream.rangeClosed(1, maximosPeriodos)
                .forEach(i -> simularPeriodo(proyectoId));
    }
}
