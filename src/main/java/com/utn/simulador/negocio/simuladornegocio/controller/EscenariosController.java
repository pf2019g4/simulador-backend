/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.service.EscenarioService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class EscenariosController {

    private final EscenarioService escenarioService;

    @GetMapping("/escenarios")
    public List<Escenario> getEscenarios() {
        return escenarioService.getEscenarios();
    }
    
    @GetMapping("/escenarios/usuario/{usuarioId}")
    public List<Escenario> getEscenariosPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return escenarioService.getEscenariosPorUsuario(usuarioId);
    }
    
    @GetMapping("/escenarios/{id}")
    public Escenario getEscenarioById(@PathVariable("id") Long id) {
        return escenarioService.getEscenarioById(id);
    }
    
    @PostMapping("/escenarios")
    public Escenario createEscenario(@RequestBody Escenario escenario) {
        return escenarioService.createEscenario(escenario);
    }
    
    @PutMapping("/escenarios/{id}")
    public Escenario modifyEscenario(@PathVariable("id") Long id,@RequestBody Escenario escenario) {
        return escenarioService.modifyEscenario(escenario);
    }
    
    @DeleteMapping("/escenarios/{id}")
    public void deleteEscenario(@PathVariable("id") Long id) {
        escenarioService.deleteEscenarioById(id);
    }

}
