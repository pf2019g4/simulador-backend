package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import com.utn.simulador.negocio.simuladornegocio.domain.Credito;
import com.utn.simulador.negocio.simuladornegocio.repository.CreditoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.FinanciacionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FinanciacionService {

    private final FinanciacionRepository financiacionRepository;
    private final CreditoRepository creditoRepository;

    public List<Financiacion> obtenerPorEscenario(Long escenarioId) {
        return financiacionRepository.findByEscenarioId(escenarioId);
    }

    public Financiacion crear(Financiacion financiacion) {
        return financiacionRepository.save(financiacion);
    }

    public void editar(Financiacion financiacion) {
        financiacionRepository.save(financiacion);
    }

    public void borrar(Long financiacionId) {
        financiacionRepository.deleteById(financiacionId);
    }

    public Credito tomar(Credito credito) {
        return creditoRepository.save(credito);
    }

    public Credito obtenerCreditoPorProyecto(Long proyectoId) {
        return creditoRepository.findByProyectoId(proyectoId);
    }

}
