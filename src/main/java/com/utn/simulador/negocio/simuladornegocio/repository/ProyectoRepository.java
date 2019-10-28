package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Proyecto findByUsuarioIdAndEscenarioId(long usaurioId, long escenarioId);

    @Modifying(clearAutomatically = true)
    @Query("update Proyecto proyecto set proyecto.entregado = true where proyecto.cursoId =:cursoId and proyecto.escenario.id = :escenarioId")
    void marcarProyectosComoEntregado(Long cursoId, Long escenarioId);

    List<Proyecto> findByCursoIdAndEscenarioId(Long cursoId, Long escenarioId);
}
