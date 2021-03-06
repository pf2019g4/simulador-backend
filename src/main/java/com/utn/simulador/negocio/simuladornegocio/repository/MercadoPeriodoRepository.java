package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface MercadoPeriodoRepository extends JpaRepository<MercadoPeriodo, Long> {

    //TODO este transactional no deberia estar
    @Transactional
    void deleteByEscenarioId(Long escenarioId);

    public List<MercadoPeriodo> findByEscenarioId(Long escenarioId);

    public MercadoPeriodo findByEscenarioIdAndPeriodo(Long id, Integer periodo);
}
