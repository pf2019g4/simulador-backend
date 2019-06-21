package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.*;

import lombok.Data;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Producto producto;
    private BigDecimal costoFijo;
    private BigDecimal costoVariable;
    private BigDecimal caja;
    private Long stock;
    private Long produccionMensual;
    private Boolean activo;
    private Integer mes; //TODO usar time en vez de contador
    @Embedded
    private ParametrosVentas parametrosVentas;

}
