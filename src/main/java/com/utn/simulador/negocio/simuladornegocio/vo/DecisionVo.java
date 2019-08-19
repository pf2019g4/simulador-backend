package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecisionVo {

    private Long id;
    private String descripcion;
    private Long opcionTomada;

    private List<Opcion> opciones;

    public DecisionVo(Decision decision, Long opcionTomada) {

        this.id = decision.getId();
        this.opcionTomada = opcionTomada;
        this.descripcion = decision.getDescripcion();
        this.opciones = decision.getOpciones();
    }
}
