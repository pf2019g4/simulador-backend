package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Forecast findByProyectoIdAndPeriodo(Long idProyecto, Integer periodo);
}
