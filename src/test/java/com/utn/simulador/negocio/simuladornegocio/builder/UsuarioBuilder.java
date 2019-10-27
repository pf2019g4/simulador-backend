package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.domain.Rol;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;

public class UsuarioBuilder extends AbstractPersistenceBuilder<Usuario> {
    
    private UsuarioBuilder() {
        instance = new Usuario();
    }
    
    public static UsuarioBuilder jugador(String mail, String nombreCompleto, Curso curso) {
        UsuarioBuilder usuarioBuilder = new UsuarioBuilder();
        usuarioBuilder.instance.setMail(mail);
        usuarioBuilder.instance.setNombreCompleto(nombreCompleto);
        usuarioBuilder.instance.setCurso(curso);
        usuarioBuilder.instance.setRol(Rol.JUGADOR);
        
        return usuarioBuilder;
    }
    
    public static UsuarioBuilder admin(String mail, String nombreCompleto, Curso curso) {
        UsuarioBuilder usuarioBuilder = new UsuarioBuilder();
        usuarioBuilder.instance.setMail(mail);
        usuarioBuilder.instance.setNombreCompleto(nombreCompleto);
        usuarioBuilder.instance.setCurso(curso);
        usuarioBuilder.instance.setRol(Rol.ADMIN);
        
        return usuarioBuilder;
    }
    
}
