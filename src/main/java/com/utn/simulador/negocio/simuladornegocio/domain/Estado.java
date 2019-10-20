package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.*;

import lombok.Data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Proyecto proyecto;

    //TODO borrar producto y la clase/tabla, no se usa para nada
    @ManyToOne
    private Producto producto;

    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private Long produccionMensual;
    private Long stock;
    private Integer calidad;

    private BigDecimal caja;
    private BigDecimal ventas;
    private BigDecimal capitalSocial;
    private BigDecimal demandaPotencial;
    private Boolean activo;
    private Integer periodo;
    private Boolean esForecast;

}
