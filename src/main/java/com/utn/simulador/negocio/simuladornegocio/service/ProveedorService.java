package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.repository.ModalidadPagoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProveedorRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.ProveedorVo;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProyectoRepository proyectoRepository;
    private final ModalidadPagoRepository modalidadPagoRepository;
    private final EstadoRepository estadoRepository;

    public List<Proveedor> obtenerPorEscenario(Long idEscenario){
        return proveedorRepository.findByEscenarioId(idEscenario);
    }
    
    public Proveedor crearProveedor(Proveedor proveedor) {
        
        //Necesario??
        for(ModalidadPago modalidadPago : proveedor.getModalidadPago()){
            modalidadPago.setProveedor(proveedor);
        }
        return proveedorRepository.save(proveedor);
    }

    public Proveedor editarProveedor(Long proveedorId, Proveedor proveedor) {

        List<ModalidadPago> modalidadesPagoBD = modalidadPagoRepository.findByProveedorId(proveedorId);
        for (ModalidadPago modalidadPago : modalidadesPagoBD) {
            if (!proveedor.getModalidadPago().contains(modalidadPago)) {
                modalidadPagoRepository.deleteById(modalidadPago.getId());
            }
        }

        return crearProveedor(proveedor);
    }

    public void borrarProveedor(Long proveedorId) {

        proveedorRepository.deleteById(proveedorId);
    }
    
    public void guardarProveedorProyecto(Long idProyecto, Long idProveedor){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).get();
        Proveedor proveedorSeleccionado = proveedorRepository.findById(idProveedor).get();
        proyecto.setProveedorSeleccionado(proveedorSeleccionado);
        proyectoRepository.save(proyecto);
    }

    public List<ProveedorVo> obtenerPorProyecto(Long idProyecto){
        
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElseThrow(() -> new IllegalArgumentException("Proyecto inexistente"));
        
        return obtenerProveedoresPorProyecto(proyecto);
        
    }
    
    private List<ProveedorVo> obtenerProveedoresPorProyecto(Proyecto proyecto) {
        List<Proveedor> proveedoresPosibles = proveedorRepository.findByEscenarioId(proyecto.getEscenario().getId());
        Proveedor proveedorSeleccionado = proyecto.getProveedorSeleccionado();
        List<ProveedorVo> proveedoresVo = new ArrayList<>();
        
        proveedoresPosibles.forEach((proveedor) -> {
            proveedoresVo.add(new ProveedorVo(proveedor, proveedorSeleccionado != null ? proveedor.getId() == proveedorSeleccionado.getId() : false));
        });
        
        return proveedoresVo;
    }
    
    public Proveedor obtenerProveedorPorDefecto(){
        
        return proveedorRepository.findByEscenarioId(null).get(0);
        
    }
    
    public void aplicarCambiosAtributos(Long proyectoId, Boolean esForecast){
        
        Estado estadoActual = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(proyectoId, esForecast);

        if(estadoActual.getProyecto().getProveedorSeleccionado() != null ){
            estadoActual.setCostoVariable(estadoActual.getCostoVariable().add(estadoActual.getProyecto().getProveedorSeleccionado().getVariacionCostoVariable()));
            estadoActual.setCalidad(estadoActual.getCalidad() + estadoActual.getProyecto().getProveedorSeleccionado().getVariacionCalidad());
        
            this.estadoRepository.save(estadoActual);
        }
    }

}
