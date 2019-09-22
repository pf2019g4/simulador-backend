package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.service.ModalidadPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ModalidadPagoController {

    private final ModalidadPagoService modalidadPagoService;

    @PostMapping("/proyecto/{proyectoId}/modalidadPago")
    public void setearModalidadCobro(@PathVariable("proyectoId") Long proyectoId, @RequestBody List<ModalidadPago> modalidadesPago) {
        modalidadPagoService.eliminarViejasModalidadesPago(proyectoId);
        modalidadPagoService.guardar(modalidadesPago);
    }
    
    @GetMapping("/proyecto/{proyectoId}/modalidadPago")
    public List<ModalidadPago> recuperarModalidadCobro(@PathVariable("proyectoId") Long proyectoId) {
        return modalidadPagoService.obtenerModalidadesPago(proyectoId);
    }
}
