package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ModalidadCobroRepository extends JpaRepository<ModalidadCobro, Long> {

    ModalidadCobro findByProyectoId(Long idProyecto);
    
    @Transactional
    void deleteByProyectoId(Long idProyecto);
}
