package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long escenarioId;
    private String nombre;
    private BigDecimal variacionCostoVariable;
    private Integer variacionCalidad;
    
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<ModalidadPago> modalidadPago;
}
