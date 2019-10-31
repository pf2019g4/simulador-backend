package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PonderacionPuntajeRepository extends JpaRepository<PonderacionPuntaje, Long> {

    public List<PonderacionPuntaje> findByEscenarioId(Long escenarioId);
}
