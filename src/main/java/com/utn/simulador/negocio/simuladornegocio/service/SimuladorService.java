package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SimuladorService {

    private final EstadoService estadoService;

    public Estado simularPeriodo(long proyectoId) {
        Estado estado = estadoService.obtenerActual();

        Estado nuevoEstado = estadoService.avanzarTiempo(estado);

        return nuevoEstado;
    }

}
