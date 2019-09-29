package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final EscenarioRepository escenarioRepository;
    private final EstadoService estadoService;

    public Estado obtener(long escenarionId, long usuarioId) {
        Estado estado = null;
        Proyecto proyecto = proyectoRepository.findByUsuarioIdAndEscenarioId(usuarioId, escenarionId);

        if (proyecto == null) {
            estado = crearProyecto(escenarionId, usuarioId);
        } else {
            estado = estadoService.obtenerActual(proyecto.getId(), true);
        }

        return estado;
    }

    private Estado crearProyecto(long escenarionId, long usuarioId) {
        Escenario escenario = escenarioRepository.findById(escenarionId).orElseThrow();

        Proyecto proyecto = Proyecto.builder()
                .usuarioId(usuarioId)
                .nombre(escenario.getTitulo())
                .escenario(escenario)
                .build();

        proyectoRepository.save(proyecto);

        Estado estado = estadoService.crearEstadoBaseParaProyecto(proyecto);
        return estado;
    }

}
