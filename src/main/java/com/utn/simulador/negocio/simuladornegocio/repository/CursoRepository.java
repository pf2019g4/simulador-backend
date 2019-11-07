package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    Curso findByNombre(String nombre);
}
