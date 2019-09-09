package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import com.utn.simulador.negocio.simuladornegocio.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ForecastController {

    private final ForecastService forecastService;

    @PostMapping("/proyecto/{proyectoId}/forecast")
    public void darDeAltaForecast(@PathVariable("proyectoId") Long proyectoId, @RequestBody List<Forecast> listaForecast) {
        forecastService.eliminarViejoForecast(proyectoId);
        forecastService.guardar(listaForecast);
    }
}
