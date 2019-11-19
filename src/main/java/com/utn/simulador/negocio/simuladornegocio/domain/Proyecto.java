package com.utn.simulador.negocio.simuladornegocio.domain;

import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "proyectoId", cascade = CascadeType.ALL)
    private List<ModalidadCobro> modalidadCobro;

    @OneToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedorSeleccionado;

    private Long usuarioId;

    @ManyToOne
    @JoinColumn(name = "escenario_id", nullable = false)
    private Escenario escenario;

    private Long cursoId;

    private Boolean entregado;

    private int ponderacionMercadoBajo;
    private int ponderacionMercadoMedio;
    private int ponderacionMercadoAlto;

    public void aumentarPonderacionMercado(PonderacionMercado ponderacionMercado) {
        ponderacionMercadoAlto += ponderacionMercado.getAlto();
        ponderacionMercadoMedio += ponderacionMercado.getMedio();
        ponderacionMercadoBajo += ponderacionMercado.getBajo();
    }

}
