package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
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
public class ModalidadPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne    
    //@JoinColumn(name = "proveedor_id", nullable = false)
    @JsonBackReference
    private Proveedor proveedor;
    private BigDecimal porcentaje;
    private Integer offsetPeriodo; // periodo 0, 1, 2, etc
}
