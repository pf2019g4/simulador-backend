package com.utn.simulador.negocio.simuladornegocio.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EscenarioUsuarioDto implements Serializable{

    private String escenario;
    private String curso;
    private String usuarioMail;
    private String usuarioNombre;
    private String usuarioFotoUrl;
    private Boolean entregado;
    
    public EscenarioUsuarioDto() {
    }
    
}
