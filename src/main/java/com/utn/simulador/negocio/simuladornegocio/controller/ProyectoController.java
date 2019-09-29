package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class ProyectoController {

    private final ProyectoService proyectoService;

    @GetMapping("/escenario/{escenacionId}/usuario/{usuarioId}/proyecto")
    public Estado obtenerEstadoPorEscenarioYUsuario(@PathVariable("escenacionId") long escenacionId,
            @PathVariable("usuarioId") long usuarioId) {
        return proyectoService.obtener(escenacionId, usuarioId);
    }

}
