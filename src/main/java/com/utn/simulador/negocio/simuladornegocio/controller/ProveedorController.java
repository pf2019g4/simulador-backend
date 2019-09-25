package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.service.ProveedorService;
import com.utn.simulador.negocio.simuladornegocio.vo.ProveedorVo;
import java.math.BigInteger;
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
        return proveedorService.obtenerPorEscenario(escenarioId);
    }

    @PostMapping("/proyecto/{proyectoId}/proveedor")
    public void setearProveedorProyecto(@PathVariable("proyectoId") Long proyectoId, @RequestBody Long proveedorId) {
        proveedorService.guardarProveedorProyecto(proyectoId, proveedorId);
    }
    
    @GetMapping("/proyecto/{proyectoId}/proveedor")
    public List<ProveedorVo> obtenerProveedoresProyecto(@PathVariable("proyectoId") Long proyectoId) {
        return proveedorService.obtenerPorProyecto(proyectoId);
    }
}
