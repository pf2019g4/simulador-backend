package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorService {

    private final EstadoService estadoService;
    private final SimuladorVentasService simuladorVentasService;
    private final SimuladorProduccionService simuladorProduccionService;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final CuentaPeriodoRepository cuentaPeriodoRepository;
    private final CuentaRepository cuentaRepository;

    public Estado simularPeriodo(long proyectoId) {
        Estado estado = avanzarTiempo();
        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);
        estadoService.guardar(estado);
        return estado;
    }

    private Estado avanzarTiempo() {
        Estado estado = estadoService.obtenerActual();
        Estado nuevoEstado = estadoService.avanzarTiempo(estado);
        return nuevoEstado;
    }

    private void deshacerSimulacionPrevia(Long proyectoId) {
        for (OpcionProyecto op: opcionProyectoRepository.findByProyectoId(proyectoId)) {
            opcionProyectoRepository.deleteById(op.getId());
            for (Cuenta cuenta : cuentaRepository.findByProyectoIdAndOpcionId(proyectoId, op.getId())) {
                for (CuentaPeriodo cp: cuenta.getCuentasPeriodo()) {
                    cuentaPeriodoRepository.deleteById(cp.getId());
                }
                cuentaRepository.deleteById(cuenta.getId());
            }
        }
        estadoService.borrarEstados(proyectoId);
    }

}
