package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Estado extends EstadoInicial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Proyecto proyecto;

    //TODO borrar producto y la clase/tabla, no se usa para nada
    @ManyToOne
    private Producto producto;

    private BigDecimal caja;
    private BigDecimal ventas;
    private BigDecimal capitalSocial;
    private BigDecimal demandaPotencial;
    private Boolean activo;
    private Integer periodo;
    private Boolean esForecast;

}
