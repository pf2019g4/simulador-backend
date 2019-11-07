package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface PonderacionMercadoRepository extends JpaRepository<PonderacionMercado, Long> {

    List<PonderacionMercado> findByEscenarioId(Long escenarioId);
    
    @Transactional
    void deleteByEscenarioId(Long escenarioId);
}
