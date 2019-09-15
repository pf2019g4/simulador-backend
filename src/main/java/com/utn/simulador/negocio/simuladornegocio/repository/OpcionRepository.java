package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {

    @Transactional
    List<Opcion> getByDecisionId(Long decisionId);
}
