/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import com.utn.simulador.negocio.simuladornegocio.service.PuntajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@Transactional
public class PuntajeController {

    private final PuntajeService puntajeService;
    
    @GetMapping("/escenario/{escenarioId}/puntajeEscenario")
    public PonderacionPuntaje obtenerMercadoEscenario(@PathVariable("escenarioId") Long escenarioId) {
        return puntajeService.obtenerPuntajeEscenario(escenarioId);
    }
    
    @PostMapping("/escenario/{escenarioId}/puntajeEscenario")
    public PonderacionPuntaje cargarPuntajeJuego(@PathVariable("escenarioId") Long escenarioId, @RequestBody PonderacionPuntaje puntaje) {
        return puntajeService.cargarPuntajeEscenario(escenarioId, puntaje);
    }

}
