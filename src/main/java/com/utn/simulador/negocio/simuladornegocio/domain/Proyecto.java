package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "proyectoId", cascade = CascadeType.ALL)
    private List<ModalidadCobro> modalidadCobro;
    
    @OneToOne
    @JoinColumn(name = "proveedor_id")
    @JsonBackReference
    private Proveedor proveedorSeleccionado;

    @ManyToOne
    @JoinColumn(name = "escenario_id", nullable = false)
    private Escenario escenario;
}
