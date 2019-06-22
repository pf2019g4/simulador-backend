package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;

    public Estado simularPeriodo(long proyectoId) {
        Estado estado = estadoService.obtenerActual();

        Estado nuevoEstado = estadoService.avanzarTiempo(estado);

        simuladorProduccionService.simular(nuevoEstado);
        simuladorVentasService.simular(nuevoEstado);

        estadoService.guardar(nuevoEstado);
        return nuevoEstado;
    }

}
