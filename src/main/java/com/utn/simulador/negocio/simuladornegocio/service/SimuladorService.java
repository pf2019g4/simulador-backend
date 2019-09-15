package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.util.stream.IntStream;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;

    private final EstadoRepository estadoRepository;
    private final ProyectoRepository proyectoRepository;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final CuentaPeriodoRepository cuentaPeriodoRepository;
    private final CuentaRepository cuentaRepository;
    private final CuentaService cuentaService;

    public Estado simularPeriodo(long proyectoId, boolean esForecast) {
        Estado estado = avanzarTiempo(proyectoId, esForecast);

        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);
        estadoService.guardar(estado);
        return estado;
    }

    public void crearPrimerEstadoSimulacion(Long proyectoId, boolean esForecast) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Estado estado = proyecto.getEscenario().getEstadoInicial();

        Estado estadoNuevo = Estado.builder()
                .activo(true)
                .caja(estado.getCaja())
                .costoFijo(estado.getCostoFijo())
                .costoVariable(estado.getCostoVariable())
                .esForecast(esForecast)
                .parametrosVentas(estado.getParametrosVentas())
                .periodo(estado.getPeriodo())
                .produccionMensual(estado.getProduccionMensual())
                .producto(estado.getProducto())
                .proyecto(proyecto)
                .stock(estado.getStock())
                .ventas(estado.getVentas())
                .build();

        estadoService.guardar(estadoNuevo);
    }

    private Estado avanzarTiempo(long proyectoId, boolean esForecast) {
        Estado estado = estadoService.obtenerActual(proyectoId, esForecast);
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        cuentaService.inputarCuetasNuevoPeriodo(nuevoEstado);

        estadoRepository.save(nuevoEstado);
        return nuevoEstado;
    }

    public void simularPeriodos(Long proyectoId, boolean esForecast) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        Integer maximosPeriodos = proyecto.getEscenario().getMaximosPeriodos();
        IntStream.rangeClosed(1, maximosPeriodos)
                .forEach(i -> simularPeriodo(proyectoId, esForecast));
    }

    //TODO: esto no deberia utilizar solo las de forecast?
    public void deshacerSimulacionPrevia(Long proyectoId) {
        for (OpcionProyecto op : opcionProyectoRepository.findByProyectoId(proyectoId)) {
            opcionProyectoRepository.deleteById(op.getId());
            for (Cuenta cuenta : cuentaRepository.findByProyectoIdAndOpcionId(proyectoId, op.getId())) {
                for (CuentaPeriodo cp : cuenta.getCuentasPeriodo()) {
                    cuentaPeriodoRepository.deleteById(cp.getId());
                }
                cuentaRepository.deleteById(cuenta.getId());
            }
        }
        estadoService.borrarEstados(proyectoId);

    }

}
