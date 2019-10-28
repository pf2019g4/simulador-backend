package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PonderacionMercadoRepository extends JpaRepository<PonderacionMercado, Long> {

    public List<PonderacionMercado> findByEscenarioId(Long escenarioId);
}
