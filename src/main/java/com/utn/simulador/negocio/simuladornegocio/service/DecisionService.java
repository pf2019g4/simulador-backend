package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepository;

    public List<Decision> obtenerPorProyecto(Long idProyecto) {
        return decisionRepository.findByProyectoId(idProyecto);
    }

    public void guardar(Decision decision) {
        decisionRepository.save(decision);
    }

}
