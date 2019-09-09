package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Escenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer periodos;
    private String descripcion;
    private Integer maximosPeriodos;
    private Double impuestoPorcentaje;  //Es un valor entre 0 y 1
    
    @OneToOne
    @JoinColumn(name = "estado_id")
    private Estado estadoInicial;
    
    
	public Long getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Integer getPeriodos() {
		return periodos;
	}

	public void setPeriodos(Integer periodos) {
		this.periodos = periodos;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Double getImpuestoPorcentaje() {
		return impuestoPorcentaje;
	}
	
	public void setImpuestoPorcentaje(Double impuestoPorcentaje) {
		this.impuestoPorcentaje = impuestoPorcentaje;
	}

}
