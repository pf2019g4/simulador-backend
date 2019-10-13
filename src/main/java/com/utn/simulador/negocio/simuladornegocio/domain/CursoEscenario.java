package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CursoEscenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cursoId;
    
    @ManyToOne
    private Escenario escenario;

}
