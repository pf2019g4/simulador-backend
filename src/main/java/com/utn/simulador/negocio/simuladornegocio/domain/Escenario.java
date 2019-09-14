package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Escenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer maximosPeriodos;
    private Double impuestoPorcentaje;  //Es un valor entre 0 y 1
    
    @OneToOne
    @JoinColumn(name = "estado_id")
    @JsonBackReference
    private Estado estadoInicial;
}
