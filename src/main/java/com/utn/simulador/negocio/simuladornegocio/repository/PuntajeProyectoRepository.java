package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PuntajeProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PuntajeProyectoRepository extends JpaRepository<PuntajeProyecto, Long> {

    PuntajeProyecto findByProyectoId(Long proyectoId);
    
    @Transactional
    void deleteByProyectoId(Long proyectoId);
}
