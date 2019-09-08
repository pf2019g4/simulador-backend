package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Forecast findByProyectoIdAndPeriodo(Long idProyecto, Integer periodo);

    @Transactional
    void deleteByProyectoId(Long idProyecto);
}
