package com.utn.simulador.negocio.simuladornegocio.domain;

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

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<ModalidadCobro> modalidadCobro;

    @ManyToOne
    @JoinColumn(name = "escenario_id", nullable = false)
    private Escenario escenario;
}
