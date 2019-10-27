package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.service.UsuarioService;
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
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/usuario")
    public Usuario obtenerUsuario(@RequestBody Usuario usuario) {
        return usuarioService.obtenerUsuario(usuario);
    }
    
    @PostMapping("/usuario/{usuarioId}/matricular")
    public void matricularseACurso(@PathVariable("usuarioId") Long usuarioId, @RequestBody Curso curso) throws Exception{
        usuarioService.matricularseACurso(usuarioId, curso);
    }
}
