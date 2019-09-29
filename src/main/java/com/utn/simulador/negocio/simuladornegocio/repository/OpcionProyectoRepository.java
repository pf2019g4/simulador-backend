package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcionProyectoRepository extends JpaRepository<OpcionProyecto, Long> {

    List<OpcionProyecto> findByProyectoId(Long proyectoId);

    List<OpcionProyecto> deleteByOpcionId(Long opcionId);

}
