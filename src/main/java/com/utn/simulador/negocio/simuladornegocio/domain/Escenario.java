package com.utn.simulador.negocio.simuladornegocio.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private Double impuestoPorcentaje;  //Es un valor entre 0 y 1

    
    
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
