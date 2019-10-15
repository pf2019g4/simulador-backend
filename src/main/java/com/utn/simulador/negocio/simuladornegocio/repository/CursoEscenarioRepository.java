package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.CursoEscenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoEscenarioRepository extends JpaRepository<CursoEscenario, Long> {

    List<CursoEscenario> findByCursoId(Long cursoId);
    
    List<CursoEscenario> findByEscenarioId(Long escenarioId);
}
