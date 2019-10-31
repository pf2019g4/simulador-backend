/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.service.ConfiguracionMercadoService;
import com.utn.simulador.negocio.simuladornegocio.vo.MercadoEscenarioVo;
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
public class ConfiguracionMercadoController {

    private final ConfiguracionMercadoService configuracionMercadoService;

    @GetMapping("/escenario/{escenarioId}/configuracionMercado")
    public MercadoEscenarioVo obtenerMercadoEscenario(@PathVariable("escenarioId") Long escenarioId) {
        return configuracionMercadoService.obtenerMercadoEscenario(escenarioId);
    }

    @PostMapping("/escenario/{escenarioId}/configuracionMercado")
    public MercadoEscenarioVo cargarMercadoEscenario(@PathVariable("escenarioId") Long escenarioId, @RequestBody MercadoEscenarioVo mercadoEscenarioVo) {
        return configuracionMercadoService.cargarMercadoEscenario(escenarioId, mercadoEscenarioVo);
    }

}
