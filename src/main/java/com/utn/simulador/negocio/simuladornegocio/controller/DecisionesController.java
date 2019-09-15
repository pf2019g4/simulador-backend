/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.DecisionService;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
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
public class DecisionesController {

    private final DecisionService decisionService;

    @GetMapping("/proyecto/{proyectoId}/decisiones")
    public List<DecisionVo> decisionesPorProyecto(@PathVariable("proyectoId") Long proyectoId) {
        return decisionService.obtenerPorProyecto(proyectoId);
    }
    
    @PostMapping("/decisiones")
    public Decision crearDecision(@RequestBody Decision decision) {
        return decisionService.crearDecision(decision);
    }
    
    @PutMapping("/decisiones/{decisionId}")
    public void editarDecision(@PathVariable("decisionId") Long decisionId, @RequestBody Decision decision) {
        decisionService.editarDecision(decisionId, decision);
    }
    
    @DeleteMapping("/decisiones/{decisionId}")
    public void borrarDecision(@PathVariable("decisionId") Long decisionId) {
        decisionService.borrarDecision(decisionId);
    }

}
