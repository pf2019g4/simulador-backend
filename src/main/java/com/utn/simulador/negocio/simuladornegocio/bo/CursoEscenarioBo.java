package com.utn.simulador.negocio.simuladornegocio.bo;

import com.utn.simulador.negocio.simuladornegocio.domain.CursoEscenario;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CursoEscenarioBo implements Serializable{

    private String escenario;
    private String curso;
    private Boolean abierto;
    private List<JugadorProyectoBo> jugadores;
    
    public CursoEscenarioBo() {
    }
    
    public CursoEscenarioBo(CursoEscenario cursoEscenario) {
        this.escenario = cursoEscenario.getEscenario().getTitulo();
        this.curso = cursoEscenario.getCurso().getNombre();
        this.abierto = cursoEscenario.getAbierto();
    }
    
}
