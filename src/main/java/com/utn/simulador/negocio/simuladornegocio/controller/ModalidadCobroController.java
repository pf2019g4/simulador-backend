package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.service.ModalidadCobroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ModalidadCobroController {

    private final ModalidadCobroService modalidadCobroService;

    @PostMapping("/proyecto/{proyectoId}/modalidadCobro")
    public void setearModalidadCobro(@PathVariable("proyectoId") Long proyectoId, @RequestBody List<ModalidadCobro> modalidadesCobro) {
        modalidadCobroService.eliminarViejasModalidadesCobro(proyectoId);
        modalidadCobroService.guardar(modalidadesCobro);
    }
    
    @GetMapping("/proyecto/{proyectoId}/modalidadCobro")
    public void setearModalidadCobro(@PathVariable("proyectoId") Long proyectoId) {
        modalidadCobroService.obtenerModalidadesCobro(proyectoId);
    }
}
