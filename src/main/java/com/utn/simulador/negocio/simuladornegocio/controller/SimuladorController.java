/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.service.DecisionService;
import com.utn.simulador.negocio.simuladornegocio.service.SimuladorService;
import java.util.List;
import java.util.Arrays; 
import lombok.RequiredArgsConstructor;
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
public class SimuladorController {

    private final DecisionService decisionService;
    private final SimuladorService simuladorService;
    
    @GetMapping("/tipoFlujoFondos")
    public List<TipoFlujoFondo> getTipoFlujoFondos() {
        return Arrays.asList(TipoFlujoFondo.values());
    }
    
    @GetMapping("/tipoCuentas")
    public List<TipoCuenta> getTipoCuentas() {
        return Arrays.asList(TipoCuenta.values());
    }
    
    @PostMapping("/proyecto/{proyectoId}/simularOpciones")
    public void tomaDecision(@PathVariable("proyectoId") Long proyectoId,
            @RequestBody List<Opcion> opciones) {
        simuladorService.deshacerSimulacionPrevia(proyectoId);
        simuladorService.crearPrimerEstadoSimulacion(proyectoId, true);
        for(Opcion opcion : opciones){
            decisionService.tomaDecision(proyectoId, opcion.getId());   
        }
        simuladorService.simularPeriodos(proyectoId, true);
    }

}
