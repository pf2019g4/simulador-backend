package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public Estado obtenerActual(long proyectoId, boolean esForecast) {
        return estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(proyectoId, esForecast);
    }

    public List<Estado> obtenerPorProyecto(Long idProyecto) {
        return estadoRepository.findByProyectoId(idProyecto).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));
    }

    public Estado avanzarTiempo(Estado estado) {
        Estado nuevoEstado = estado.toBuilder().id(null).build();
        estado.setActivo(Boolean.FALSE);
        nuevoEstado.setPeriodo(estado.getPeriodo() + 1);

        estadoRepository.save(nuevoEstado);
        estadoRepository.save(estado);
        return nuevoEstado;
    }

    void guardar(Estado estado) {
        estadoRepository.save(estado);
    }

    public void borrarEstadosForecast(Long idProyecto) {
       estadoRepository.deleteByProyectoIdAndEsForecast(idProyecto, true);
    }

}
