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
    @OneToOne
    private Proyecto proyecto;
    @ManyToOne
    private Producto producto;
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private BigDecimal caja;
    private BigDecimal ventas;
    private Integer calidad;
    private BigDecimal maquinarias;
    private BigDecimal amortizacionAcumulada;
    private BigDecimal capitalSocial;
    private BigDecimal resultadoDelEjercicio;
    private Long stock;
    private Long produccionMensual;
    private BigDecimal demandaInsatisfecha;
    private Boolean activo;
    private Integer periodo; //TODO usar time en vez de contador
    @Embedded
    private ParametrosVentas parametrosVentas;
    private Boolean esForecast;

}
