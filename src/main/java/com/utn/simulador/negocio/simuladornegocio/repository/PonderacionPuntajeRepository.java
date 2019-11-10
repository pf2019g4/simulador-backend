package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PonderacionPuntajeRepository extends JpaRepository<PonderacionPuntaje, Long> {

    public PonderacionPuntaje findByEscenarioId(Long escenarioId);
    
    @Transactional
    void deleteByEscenarioId(Long escenarioId);
}
