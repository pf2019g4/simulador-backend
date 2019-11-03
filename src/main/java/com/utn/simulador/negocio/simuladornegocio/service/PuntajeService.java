package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionPuntajeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuntajeService {

    private final EscenarioRepository escenarioRepository;
    private final PonderacionPuntajeRepository ponderacionPuntajeRepository;

    public PonderacionPuntaje obtenerPuntajeEscenario(Long escenarioId) {
        
        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.findByEscenarioId(escenarioId);
        }
        return null;
    }
    
    public PonderacionPuntaje cargarPuntajeEscenario(Long escenarioId, PonderacionPuntaje ponderacionPuntaje) {

        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.save(ponderacionPuntaje);
        }
        return null;
    }

}
