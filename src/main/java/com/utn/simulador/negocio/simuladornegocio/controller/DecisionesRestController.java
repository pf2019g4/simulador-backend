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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class DecisionesRestController {

    private final DecisionService decisionService;

    @GetMapping("/proyecto/{proyectoId}/decisiones")
    public List<DecisionVo>  decisionesPorProyecto(@PathVariable("proyectoId") Long proyectoId) {

        return decisionService.obtenerPorProyecto(proyectoId);
//        return "[ { id: 1, descripcion: 'Cuanto quiere invertir en publicidad?', opcionSeleccionada: 2, opciones: [	 { id: 1, descripcion: 'Invertir $2000', consecuencias: [{ cuenta: 'Caja', valor: 20 }, { cuenta: 'Stock', valor: 20 }, { cuenta: 'Ventas', valor: 20 }, { cuenta: 'Caja', valor: 20 }] }, { id:2, descripcion: 'Invertir $3000' }, { id:3, descripcion: 'Invertir $4000' }	 ] }, { id: 2, descripcion: 'Cuanto quiere invertir en cosas?', opciones: [{ id: 1, descripcion: 'Invertir $2000' }, { id: 2, descripcion: 'Invertir $3000' }, { id: 3, descripcion: 'Invertir $4000' }] }, { id: 3, descripcion: 'Cuanto quiere invertir en comida para perro?', opciones: [{ id: 1, descripcion: 'Invertir $2000' }, { id: 2, descripcion: 'Invertir $3000' }, { id: 3, descripcion: 'Invertir $4000' }] }, { id: 3, descripcion: 'Cuanto quiere invertir en otras cosas?', opciones: [{ id: 1, descripcion: 'Invertir $2000' }, { id: 2, descripcion: 'Invertir $3000' }, { id: 3, descripcion: 'Invertir $4000' }] } ]";
    }

}
