package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
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
    private String nombrePeriodos;
    private Double impuestoPorcentaje;  //Es un valor entre 0 y 1
    
    @OneToMany(mappedBy = "escenarioId", cascade = CascadeType.ALL)
    private List<Proveedor> proveedores;
    
    @OneToOne
    @JoinColumn(name = "estado_id")
    @JsonBackReference
    private Estado estadoInicial;

    @OneToOne
    @JoinColumn(name = "balance_id")
    @JsonBackReference
    private Balance balanceInicial;
}
