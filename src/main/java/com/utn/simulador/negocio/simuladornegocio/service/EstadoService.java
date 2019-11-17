package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
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

    public Estado crearEstadoBaseParaProyecto(Proyecto proyecto, boolean esForecast) {
        Estado nuevoEstado = Estado.builder()
                .id(null)
                .proyecto(proyecto)
                .caja(proyecto.getEscenario().getBalanceInicial().getActivo().getCaja())
                .costoFijo(proyecto.getEscenario().getEstadoInicial().getCostoFijo())
                .costoVariable(proyecto.getEscenario().getEstadoInicial().getCostoVariable())
                .produccionMensual(proyecto.getEscenario().getEstadoInicial().getProduccionMensual())
                .stock(proyecto.getEscenario().getEstadoInicial().getStock())
                .inventario(proyecto.getEscenario().getBalanceInicial().getActivo().getInventario())
                .calidad(proyecto.getEscenario().getEstadoInicial().getCalidad())
                .publicidad(proyecto.getEscenario().getEstadoInicial().getPublicidad())
                .cantidadVendedores(proyecto.getEscenario().getEstadoInicial().getCantidadVendedores())
                .periodo(0)
                .capitalSocial(proyecto.getEscenario().getBalanceInicial().getPatrimonioNeto().getCapitalSocial())
                .activo(Boolean.TRUE)
                .esForecast(esForecast).build();
        estadoRepository.save(nuevoEstado);
        return nuevoEstado;
    }

    public List<Estado> obtenerPorProyecto(Long idProyecto, Boolean esForecast) {
        return estadoRepository.findByProyectoIdAndEsForecast(idProyecto, esForecast).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));
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
