package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CreditoRepository extends JpaRepository<Credito, Long> {

    Credito findByProyectoId(Long proyectoId);

    @Transactional
    void deleteByProyectoId(Long proyectoId);
}
