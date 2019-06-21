package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.service.EstadoService;
import com.utn.simulador.negocio.simuladornegocio.service.ProductoService;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping("/estado/actual")
    public Estado obtenerEstadoActual() {
        return estadoService.obtenerActual();
    }

}
