package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.RestriccionPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestriccionPrecioRepository extends JpaRepository<RestriccionPrecio, Long> {

    public List<RestriccionPrecio> findByEscenarioId(Long escenarioId);
}
