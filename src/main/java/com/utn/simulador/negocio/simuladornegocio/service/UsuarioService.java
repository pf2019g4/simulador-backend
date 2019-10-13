package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Rol;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.repository.CursoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public Usuario obtenerPorEmail(String mail) {

        Usuario usuario = usuarioRepository.findByMail(mail);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setMail(mail);
            usuario.setRol(Rol.JUGADOR);
            usuario = usuarioRepository.save(usuario);
        }

        return usuario;
    }
    
    public void matricularseACurso(Long usuarioId, Curso curso) throws Exception {
        
        Usuario usuarioDB = usuarioRepository.findById(usuarioId).orElseThrow();
        Curso cursoDB = cursoRepository.findByNombre(curso.getNombre());
        
        if(cursoDB != null) {
            if(cursoDB.getClave().equals(curso.getClave())) {

                usuarioDB.setCurso(cursoDB);
                usuarioRepository.save(usuarioDB);
            } else {
                throw new IllegalArgumentException("Clave de curso incorrecta");
            }
        } else {
            throw new IllegalArgumentException("Curso inexistente");
        }
    }

}
