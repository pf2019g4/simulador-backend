package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CursoEscenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne 
    private Curso curso;
    
    @ManyToOne
    private Escenario escenario;
    
    private Boolean abierto;

    public CursoEscenario(Curso curso, Escenario escenario){
        this.curso = curso;
        this.escenario = escenario;
    }
    
    public CursoEscenario() {
        super();
    }
    
}
