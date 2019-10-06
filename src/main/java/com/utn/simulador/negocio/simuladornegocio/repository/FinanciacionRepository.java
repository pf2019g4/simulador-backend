package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanciacionRepository extends JpaRepository<Financiacion, Long> {

    List<Financiacion> findByEscenarioId(Long escenarioId);

}
