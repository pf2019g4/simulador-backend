/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.DecisionService;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/proyecto/{proyectoId}/opcion/{opcionId}/toma-decision")
    public void tomaDecision(@PathVariable("proyectoId") Long proyectoId,
            @PathVariable("opcionId") Long opcionId) {
        decisionService.tomaDecision(proyectoId,opcionId);

    }

}
