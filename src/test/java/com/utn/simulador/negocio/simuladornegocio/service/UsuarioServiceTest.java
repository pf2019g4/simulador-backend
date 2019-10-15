package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.CursoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.UsuarioBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void crearUsuarioYMatricularlo() {
        Curso curso1 = CursoBuilder.base("cursoPrueba", "PRU3B4").build(em);
        Usuario usuario = UsuarioBuilder.jugador("prueba@prueba.com", null).build(em);
        
        Curso cursoAMatricular = CursoBuilder.base("cursoPrueba", "PRU3B4").build();
        
        try {
            usuarioService.matricularseACurso(usuario.getId(), cursoAMatricular);
            usuario = usuarioRepository.findById(usuario.getId()).orElseThrow();
        } 
        catch(Exception e) {
            
        }
        
        assertThat(usuario.getCurso().getId().equals(curso1.getId()));
    }
    
    @Test(expected = Exception.class)
    public void crearUsuarioYMatricularloConPasswordIncorrecta() throws Exception {
        Curso curso1 = CursoBuilder.base("cursoPrueba", "PRU3B4").build(em);
        Usuario usuario = UsuarioBuilder.jugador("prueba@prueba.com", null).build(em);
        
        Curso cursoAMatricular = CursoBuilder.base("cursoPrueba", "Pass").build();
        
        usuarioService.matricularseACurso(usuario.getId(), cursoAMatricular);
    }

}
