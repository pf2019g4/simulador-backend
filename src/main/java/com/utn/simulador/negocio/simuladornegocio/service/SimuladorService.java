package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
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

    public Estado simularPeriodo(long proyectoId) {
        Estado estado = avanzarTiempo();
        simuladorProduccionService.simular(estado);
        simuladorVentasService.simular(estado);
        estadoService.guardar(estado);
        return estado;
    }
    
    public void crearPrimerEstadoSimulacion(Long proyectoId) {
        
        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();
        //Estado estado = proyecto.getEscenario().getEstado();
        
        //estado.setId(null); //Esto me crea una copia del estado inicial del escenario
        //estado.setProyecto(proyecto);
        
        //estadoService.guardar(estado);
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
