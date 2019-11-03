package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Balance;
import com.utn.simulador.negocio.simuladornegocio.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/proyecto/{proyectoId}/balance-final-forecast")
    public Balance balanceFinal(@PathVariable("proyectoId") Long proyectoId) {
        return balanceService.obtenerPorProyecto(proyectoId, true);
    }

    @GetMapping("/proyecto/{proyectoId}/balance-final")
    public Balance balanceFinalMercado(@PathVariable("proyectoId") Long proyectoId) {
        return balanceService.obtenerPorProyecto(proyectoId, false);
    }

}
