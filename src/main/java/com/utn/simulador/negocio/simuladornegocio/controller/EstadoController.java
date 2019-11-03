package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.EstadoService;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping("/proyecto/{id}/estado/actual")
    public Estado obtenerEstadoActual(@PathVariable("id") Long idProyecto) {
        return estadoService.obtenerActual(idProyecto, true);
    }

    @GetMapping("/proyecto/{id}/estado/base")
    public Estado obtenerEstadoBase(@PathVariable("id") Long idProyecto) {
        return estadoService.obtenerActual(idProyecto, false);
    }

    @GetMapping("/proyecto/{id}/estado-forecast")
    public List<Estado> obtenerPorProyecto(@PathVariable("id") Long idProyecto) {
        return estadoService.obtenerPorProyecto(idProyecto, true);
    }

}
