package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Forecast findByProyectoIdAndPeriodo(Long idProyecto, Integer periodo);
    
    List<Forecast> findByProyectoId(Long idProyecto);

    @Transactional
    void deleteByProyectoId(Long idProyecto);
}
