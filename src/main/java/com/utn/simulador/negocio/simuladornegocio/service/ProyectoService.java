package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.repository.CreditoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ForecastRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ModalidadCobroRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PuntajeProyectoRepository;
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
    private final EstadoRepository estadoRepository;
    private final CuentaRepository cuentaRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final ForecastRepository forecastRepository;
    private final ModalidadCobroRepository modalidadCobroRepository;
    private final PuntajeProyectoRepository puntajeProyectoRepository;
    private final CreditoRepository creditoRepository;
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
    
      public List<Proyecto> obtenerPorEscenario(Long escenarioId) {
        return proyectoRepository.findByEscenarioId(escenarioId);
    }

    public void cerrarProyectos(Long cursoId, Long escenarioId) {
        proyectoRepository.marcarProyectosComoEntregado(cursoId, escenarioId);
    }
    
    public void deleteDatosProyecto (Long proyectoId) {
        estadoRepository.deleteByProyectoId(proyectoId);
        //cuentaPeriodoRepository.deleteByProyectoId(proyectoId);
        cuentaRepository.deleteByProyectoId(proyectoId);
        opcionProyectoRepository.deleteByProyectoId(proyectoId);
        forecastRepository.deleteByProyectoId(proyectoId);
        modalidadCobroRepository.deleteByProyectoId(proyectoId);
        puntajeProyectoRepository.deleteByProyectoId(proyectoId);
        creditoRepository.deleteByProyectoId(proyectoId);
    }

}
