package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.bo.JugadorProyectoBo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface EscenarioRepository extends JpaRepository<Escenario, Long> {

    public List<Escenario> findAll();
    
    public Optional<Escenario> findById(Long id);
    
    public Escenario save(Escenario escenario);
    
    @Query("SELECT new com.utn.simulador.negocio.simuladornegocio.bo.JugadorProyectoBo(U.mail, U.nombreCompleto, U.fotoUrl, P.entregado, PP.cajaFinal, PP.ventasTotales, PP.renta, PP.puntaje) " +
            "FROM Proyecto P " +
            "LEFT JOIN PuntajeProyecto PP ON PP.proyectoId = P.id " +
            "INNER JOIN Usuario U ON U.id = P.usuarioId " +
            "WHERE P.escenario.id = ?1 AND U.curso.id = ?2 ")
    public List<JugadorProyectoBo> getDetalleEscenarioJugadoresPorCurso(Long escenarioId, Long cursoId);
    
}
