package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.Entity;

import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Estado {

    private Producto producto;
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private BigDecimal caja;
    private Long stock;
    private Integer mes; //TODO usar time en vez de contador
    private Long produccionMensual;


}
