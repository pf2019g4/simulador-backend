package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public Estado obtenerActual() {
        return estadoRepository.findByActivoTrue();
    }

    public Estado avanzarTiempo(Estado estado) {
        Estado nuevoEstado = estado.toBuilder().id(null).build();
        estado.setActivo(Boolean.FALSE);
        nuevoEstado.setMes(estado.getMes() + 1);
        
        estadoRepository.save(nuevoEstado);
        estadoRepository.save(estado);
        return nuevoEstado;
    }

}
