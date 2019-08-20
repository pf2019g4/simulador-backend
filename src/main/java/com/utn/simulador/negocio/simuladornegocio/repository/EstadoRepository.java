package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long>{

    public Estado findByActivoTrue();

    public Estado findByProyectoIdAndActivoTrue(Long idProyecto);
}
