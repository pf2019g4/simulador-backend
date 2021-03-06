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
public class PonderacionPuntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long escenarioId;
    private BigDecimal porcentajeCaja;
    private BigDecimal porcentajeVentas;
    private BigDecimal porcentajeRenta;
    private BigDecimal porcentajeEscenario;
    
}
