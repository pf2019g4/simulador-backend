package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Long decisionId;

    @OneToMany(mappedBy = "opcionId", cascade = CascadeType.ALL)
    private List<Consecuencia> consecuencias;

}
