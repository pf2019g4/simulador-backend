package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario obtenerPorEmail(String mail) {

        Usuario usuario = usuarioRepository.findByMail(mail);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setMail(mail);
            usuario = usuarioRepository.save(usuario);
        }

        return usuario;
    }

}
