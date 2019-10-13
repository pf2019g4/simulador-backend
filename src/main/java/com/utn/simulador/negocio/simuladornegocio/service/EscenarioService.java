package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.repository.CursoEscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EscenarioService {

    private final EscenarioRepository escenarioRepository;
    private final CursoEscenarioRepository cursoEscenarioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Escenario> getEscenarios() {
        return escenarioRepository.findAll();
    }
    
    public List<Escenario> getEscenariosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        
        return cursoEscenarioRepository.findByCursoId(usuario.getCurso().getId()).stream().map(ce -> ce.getEscenario()).collect(Collectors.toList());
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
    	escenarioToUpdate.setMaximosPeriodos(escenario.getMaximosPeriodos());
        escenarioToUpdate.setNombrePeriodos(escenario.getNombrePeriodos());
    	escenarioToUpdate.setDescripcion(escenario.getDescripcion());
    	escenarioToUpdate.setImpuestoPorcentaje(escenario.getImpuestoPorcentaje());
    	escenarioToUpdate.setBalanceInicial(escenario.getBalanceInicial());
    	return escenarioRepository.save(escenarioToUpdate);
    }
    
    public void deleteEscenarioById(Long id) {
    	escenarioRepository.deleteById(id);
    }

}
