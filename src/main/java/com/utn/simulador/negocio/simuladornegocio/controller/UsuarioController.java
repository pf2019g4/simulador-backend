package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.service.UsuarioService;
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
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/usuario/{mail}")
    public Usuario obtenerPorMail(@PathVariable("mail") String mail) {
        return usuarioService.obtenerPorEmail(mail);
    }

}
