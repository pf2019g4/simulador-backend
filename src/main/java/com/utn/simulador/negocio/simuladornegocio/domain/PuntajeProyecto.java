package com.utn.simulador.negocio.simuladornegocio.domain;

import java.math.BigDecimal;
import lombok.Data;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntajeProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long proyectoId;
    private BigDecimal cajaFinal;
    private BigDecimal ventasTotales;
    private BigDecimal renta;
    private BigDecimal puntaje;
    
}
