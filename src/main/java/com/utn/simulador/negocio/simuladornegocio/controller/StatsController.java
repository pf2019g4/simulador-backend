package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.service.ProductoService;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatsController {

    private final ProductoService productoService;

    @GetMapping("/proyecto/stat")
    public Estado obtenerStatProyecto() {

        Producto producto = productoService.obtenerProducto();

        return Estado.builder().producto(producto).build();
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
