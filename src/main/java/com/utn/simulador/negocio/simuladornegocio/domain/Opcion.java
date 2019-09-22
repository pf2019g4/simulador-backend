package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    
    @ManyToOne
    //@JoinColumn(name = "decision_id", nullable = false)
    @JsonBackReference
    private Decision decision;

    private BigDecimal variacionCostoFijo;
    private BigDecimal variacionCostoVariable;
    private Long variacionProduccion;

    @OneToMany(mappedBy = "opcion", cascade = CascadeType.ALL)
    private List<Consecuencia> consecuencias;

    public List<Cuenta> obtenerCuentasACrear(Proyecto proyecto) {
        ArrayList<Cuenta> cuentas = new ArrayList<>();

        for (Consecuencia consecuencia : consecuencias) {
            cuentas.add(consecuencia.obtenerCuenta(proyecto));
        }
        return cuentas;
    }

}
