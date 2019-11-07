package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface OpcionProyectoRepository extends JpaRepository<OpcionProyecto, Long> {

    List<OpcionProyecto> findByProyectoId(Long proyectoId);

    @Transactional
    List<OpcionProyecto> deleteByOpcionId(Long opcionId);

    @Transactional
    void deleteByProyectoId(Long proyectoId);
}
