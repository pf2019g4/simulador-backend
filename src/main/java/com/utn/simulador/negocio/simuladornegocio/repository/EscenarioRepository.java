package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EscenarioRepository extends JpaRepository<Escenario, Long> {
}
