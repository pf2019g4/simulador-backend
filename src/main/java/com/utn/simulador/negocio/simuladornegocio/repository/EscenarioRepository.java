package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.dto.EscenarioUsuarioDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface EscenarioRepository extends JpaRepository<Escenario, Long> {

    public List<Escenario> findAll();
    
    public Optional<Escenario> findById(Long id);
    
    public Escenario save(Escenario escenario);
    
    @Query("SELECT new com.utn.simulador.negocio.simuladornegocio.dto.EscenarioUsuarioDto(E.titulo, C.nombre, U.mail, P.entregado) " +
            "FROM Escenario E " +
            "INNER JOIN Proyecto P ON P.escenario.id = E.id " +
            "INNER JOIN Usuario U ON U.id = P.usuarioId " +
            "INNER JOIN Curso C ON C.id = U.curso.id " +
            "WHERE E.id = ?1 AND C.id = ?2")
    public List<EscenarioUsuarioDto> getDetalleEscenarioUsuariosPorCurso(Long escenarioId, Long cursoId);
    
}
