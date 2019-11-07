package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.EmpresaCompetidora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface EmpresasCompetidorasRepository extends JpaRepository<EmpresaCompetidora, Long> {

    List<EmpresaCompetidora> findByEscenarioId(Long escenarioId);
    
    @Transactional
    void deleteByEscenarioId(Long escenarioId);
}
