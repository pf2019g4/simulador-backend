package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import com.utn.simulador.negocio.simuladornegocio.domain.Credito;
import com.utn.simulador.negocio.simuladornegocio.service.FinanciacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class FinanciacionController {

    private final FinanciacionService financiacionService;

    @GetMapping("/escenario/{escenarioId}/financiacion")
    public List<Financiacion> obtenerCreditosEscenario(@PathVariable("escenarioId") Long escenarioId) {
        return financiacionService.obtenerPorEscenario(escenarioId);
    }

    @PostMapping("/financiacion")
    public Financiacion crear(@RequestBody Financiacion financiacion) {
        return financiacionService.crear(financiacion);
    }

    @PutMapping("/financiacion/{financiacionId}")
    public void editar(@RequestBody Financiacion financiacion) {
        financiacionService.editar(financiacion);
    }

    @PostMapping("/proyecto/{proyectoId}/credito")
    public void tomarCredito(@RequestBody Credito credito) {
        financiacionService.tomar(credito);
    }

    @DeleteMapping("/financiacion/{financiacionId}")
    public void borrar(@PathVariable("financiacionId") Long financiacionId) {
        financiacionService.borrar(financiacionId);
    }

    @GetMapping("/proyecto/{proyectoId}/financiacionTomado")
    public Credito obtenerPorProyecto(@PathVariable("proyectoId") Long proyectoId) {
        return financiacionService.obtenerCreditoPorProyecto(proyectoId);
    }
}
