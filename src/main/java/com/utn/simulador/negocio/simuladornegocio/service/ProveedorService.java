package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.ProveedorRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.ProveedorVo;
import java.util.ArrayList;
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

    public List<Proveedor> obtenerPorEscenario(Long idEscenario){
        return proveedorRepository.findByEscenarioId(idEscenario);
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

}
