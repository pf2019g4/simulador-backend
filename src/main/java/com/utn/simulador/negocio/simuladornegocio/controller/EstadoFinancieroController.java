/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.FlujoFondoService;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import java.util.Map;
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
public class EstadoFinancieroController {

    private final FlujoFondoService flujoFondoService;

    @GetMapping("/proyecto/{id}/presupuesto-financiero-forecast")
    public Map<String, AgrupadorVo> obtenerPresupuestoFinancieroForecast(@PathVariable("id") Long id) {
        return flujoFondoService.obtenerFlujoFinanciero(id, true);
    }

    @GetMapping("/proyecto/{id}/presupuesto-financiero")
    public Map<String, AgrupadorVo> obtenerPresupuestoFinanciero(@PathVariable("id") Long id) {
        return flujoFondoService.obtenerFlujoFinanciero(id, false);
    }

}
