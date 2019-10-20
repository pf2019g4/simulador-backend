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
    @ManyToOne
    private Producto producto;
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private Long stock;
    private Long produccionMensual;
    private BigDecimal caja;
    private BigDecimal ventas;
    private Integer calidad;
    private BigDecimal capitalSocial;   //TODO moverlo al escenario porque nunca cambia
    private BigDecimal demandaPotencial;
    private Boolean activo;
    private Integer periodo; //TODO usar time en vez de contador
    private Boolean esForecast;

}
