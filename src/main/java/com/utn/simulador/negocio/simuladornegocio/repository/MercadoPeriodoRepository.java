package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MercadoPeriodoRepository extends JpaRepository<MercadoPeriodo, Long> {

    public List<MercadoPeriodo> findByEscenarioId(Long escenarioId);

    public MercadoPeriodo findByEscenarioIdAndPeriodo(Long id, Integer periodo);
}
