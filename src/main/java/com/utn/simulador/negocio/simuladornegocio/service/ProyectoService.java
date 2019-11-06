package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final EscenarioRepository escenarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoService estadoService;

    public Estado obtener(long escenarionId, long usuarioId) {
        Estado estado = null;
        Proyecto proyecto = proyectoRepository.findByUsuarioIdAndEscenarioId(usuarioId, escenarionId);

        if (proyecto == null) {
            estado = crearProyecto(escenarionId, usuarioId);
        } else {
            estado = estadoService.obtenerActual(proyecto.getId(), false);
        }

        return estado;
    }

    private Estado crearProyecto(long escenarionId, long usuarioId) {
        Escenario escenario = escenarioRepository.findById(escenarionId).orElseThrow();

        Usuario usuario = usuarioRepository.findById(usuarioId).get();

        Proyecto proyecto = Proyecto.builder()
                .usuarioId(usuarioId)
                .escenario(escenario)
                .cursoId(usuario.getCurso().getId())
                .entregado(false)
                .build();
        proyectoRepository.save(proyecto);

        return estadoService.crearEstadoBaseParaProyecto(proyecto, false);
    }

    public Proyecto obtenerProyecto(long proyectoId) {
        return proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));
    }

    public Proyecto entregar(long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));
        proyecto.setEntregado(Boolean.TRUE);

        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> obtenerPorCursoYEscenario(Long cursoId, Long escenarioId) {
        return proyectoRepository.findByCursoIdAndEscenarioId(cursoId, escenarioId);
    }

    public void cerrarProyectos(Long cursoId, Long escenarioId) {
        proyectoRepository.marcarProyectosComoEntregado(cursoId, escenarioId);
    }

}
