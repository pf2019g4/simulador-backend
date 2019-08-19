package com.utn.simulador.negocio.simuladornegocio.controller;


import com.utn.simulador.negocio.simuladornegocio.service.FlujoFondoService;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class FlujoFondoController {

    private final FlujoFondoService flujoFondoService;

    @GetMapping("/proyecto/{id}/flujo-fondo")
    public Map<String, AgrupadorVo> obtenerFlujoFondo(@PathVariable("id") Long id){
        return flujoFondoService.calcularCuentas(id);
    }



}
