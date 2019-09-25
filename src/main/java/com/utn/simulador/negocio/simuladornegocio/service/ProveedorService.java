package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.ProveedorRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProyectoRepository proyectoRepository;

    public void guardarProveedoresEscenario(Long idEscenario, List<Proveedor> listaProveedores){
        proveedorRepository.saveAll(listaProveedores);
    }

    public List<Proveedor> obtenerProveedoresEscenario(Long idEscenario){
        return proveedorRepository.findByEscenarioId(idEscenario);
    }
    
    public void guardarProveedorProyecto(Long idProyecto, Long idProveedor){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).get();
        Proveedor proveedor = proveedorRepository.findById(idProveedor).get();
        proyecto.setProveedorSeleccionado(proveedor);
        proyectoRepository.save(proyecto);
    }

    public Proveedor obtenerProveedorProyecto(Long idProyecto){
        return proyectoRepository.findById(idProyecto).get().getProveedorSeleccionado();
    }

}
