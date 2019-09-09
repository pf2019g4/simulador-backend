package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EscenarioService {

    private final EscenarioRepository escenarioRepository;

    public List<Escenario> getEscenarios() {
        return escenarioRepository.findAll();
    }
    
    public Escenario getEscenarioById(Long id) {
        return escenarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));
    }
    
    public Escenario createEscenario(Escenario escenario) {
    	return escenarioRepository.save(escenario);
    }
    
    public Escenario modifyEscenario(Escenario escenario) {
    	Escenario escenarioToUpdate = escenarioRepository.getOne(escenario.getId());
    	escenarioToUpdate.setTitulo(escenario.getTitulo());
    	escenarioToUpdate.setPeriodos(escenario.getPeriodos());
    	escenarioToUpdate.setDescripcion(escenario.getDescripcion());
    	escenarioToUpdate.setImpuestoPorcentaje(escenario.getImpuestoPorcentaje());
    	return escenarioRepository.save(escenarioToUpdate);
    }
    
    public void deleteEscenarioById(Long id) {
    	escenarioRepository.deleteById(id);
    }

}
