package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.EmpresaCompetidora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresasCompetidorasRepository extends JpaRepository<EmpresaCompetidora, Long> {

    public List<EmpresaCompetidora> findByEscenarioId(Long escenarioId);
}
