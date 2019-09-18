package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ModalidadPagoRepository extends JpaRepository<ModalidadPago, Long> {

    List<ModalidadPago> findByProyectoId(Long idProyecto);
    
    @Transactional
    void deleteByProyectoId(Long idProyecto);
}
