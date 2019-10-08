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
public class EstadoEconomicoController {

    private final FlujoFondoService flujoFondoService;

    @GetMapping("/proyecto/{id}/presupuesto-economico-forecast")
    public Map<String, AgrupadorVo> obtenerPresupuestoEconomicoForecast(@PathVariable("id") Long id) {
        return flujoFondoService.obtenerFlujoEconomico(id, true);
    }

    @GetMapping("/proyecto/{id}/presupuesto-economico")
    public Map<String, AgrupadorVo> obtenerPresupuestoFinanciero(@PathVariable("id") Long id) {
        return flujoFondoService.obtenerFlujoEconomico(id, false);
    }

}
