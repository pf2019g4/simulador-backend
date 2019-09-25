package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ProveedorController {

    private final ProveedorService proveedorService;
    
    @PostMapping("/escenario/{escenarioId}/proveedores")
    public void setearProveedoresEscenario(@PathVariable("escenarioId") Long escenarioId, @RequestBody List<Proveedor> proveedores) {
        proveedorService.guardarProveedoresEscenario(escenarioId, proveedores);
    }
    
    @GetMapping("/escenario/{escenarioId}/proveedores")
    public List<Proveedor> obtenerProveedoresEscenario(@PathVariable("escenarioId") Long escenarioId) {
        return proveedorService.obtenerProveedoresEscenario(escenarioId);
    }

    @PostMapping("/proyecto/{proyectoId}/proveedor/{proveedorId}")
    public void setearProveedorProyecto(@PathVariable("proyectoId") Long proyectoId, @PathVariable("proveedorId") Long proveedorId) {
        proveedorService.guardarProveedorProyecto(proyectoId, proveedorId);
    }
    
    @GetMapping("/proyecto/{proyectoId}/proveedor")
    public Proveedor obtenerProveedorProyecto(@PathVariable("proyectoId") Long proyectoId) {
        return proveedorService.obtenerProveedorProyecto(proyectoId);
    }
}
