package com.utn.simulador.negocio.simuladornegocio.domain;

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
public class MercadoPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long escenarioId;
    private Integer periodo;
    private Integer bajo;
    private Integer medio;
    private Integer alto;
    
}
