package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EscenarioRepository extends JpaRepository<Escenario, Long> {

    public List<Escenario> findAll();
    
    public Optional<Escenario> findById(Long id);
    
    public Escenario save(Escenario escenario);
    
}
