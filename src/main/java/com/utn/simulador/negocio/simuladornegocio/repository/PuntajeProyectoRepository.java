package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PuntajeProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuntajeProyectoRepository extends JpaRepository<PuntajeProyecto, Long> {

    public PuntajeProyecto findByProyectoId(Long proyectoId);
}
