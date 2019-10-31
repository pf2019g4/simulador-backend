package com.utn.simulador.negocio.simuladornegocio.domain;

import java.math.BigDecimal;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PonderacionMercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long escenarioId;
    private String concepto;
    private BigDecimal valor;
    private BigDecimal bajo;
    private BigDecimal medio;
    private BigDecimal alto;
    
}
