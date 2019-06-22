package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.EstadoService;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.service.SimuladorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;
    private final SimuladorService simuladorService;

    @GetMapping("/estado/actual")
    public Estado obtenerEstadoActual() {
        return estadoService.obtenerActual();
    }

    @PostMapping("/estado")
    public Estado avanzarPeriodo() {
        //TODO recibir el id del proyecto por parámetro.
        return simuladorService.simularPeriodo(estadoService.obtenerActual().getProducto().getProyectoId());
    }
}
