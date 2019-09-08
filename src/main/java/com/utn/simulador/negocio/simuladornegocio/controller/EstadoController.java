package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.EstadoService;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.service.SimuladorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class EstadoController {

    private final EstadoService estadoService;
    private final SimuladorService simuladorService;

    @GetMapping("/proyecto/{id}/estado/actual")
    public Estado obtenerEstadoActual(@PathVariable("id") Long idProyecto) {
        return estadoService.obtenerActual(idProyecto, false);
    }

    @GetMapping("/proyecto/{id}/estado")
    public List<Estado> obtenerPorProyecto(@PathVariable("id") Long idProyecto) {
        return estadoService.obtenerPorProyecto(idProyecto);
    }

    @PostMapping("/proyecto/{id}/estado")
    public Estado avanzarPeriodo(@PathVariable("id") Long idProyecto) {
        //TODO recibir el id del proyecto por parámetro.
        return simuladorService.simularPeriodo(estadoService.obtenerActual(idProyecto, false).getProyecto().getId(), false);
    }
}
