package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

    public Producto findByProyectoId(long proyectoId);
    
}
