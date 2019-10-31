package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionPuntajeRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuntajeService {

    private final EscenarioRepository escenarioRepository;
    private final PonderacionPuntajeRepository ponderacionPuntajeRepository;

    public List<PonderacionPuntaje> obtenerPuntajeEscenario(Long escenarioId) {
        
        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.findByEscenarioId(escenarioId);
        }
        return null;
    }
    
    public List<PonderacionPuntaje> cargarPuntajeEscenario(Long escenarioId, List<PonderacionPuntaje> ponderacionPuntajes) {

        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.saveAll(ponderacionPuntajes);
        }
        return null;
    }

}
