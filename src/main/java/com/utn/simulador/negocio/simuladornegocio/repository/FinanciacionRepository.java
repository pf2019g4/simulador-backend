package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FinanciacionRepository extends JpaRepository<Financiacion, Long> {

    List<Financiacion> findByEscenarioId(Long escenarioId);
    
    @Transactional
    void deleteByEscenarioId(Long escenarioId);

}
