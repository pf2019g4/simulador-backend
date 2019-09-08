package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {

    @Transactional
    Long deleteByDecisionId(Long decisionId);
}
