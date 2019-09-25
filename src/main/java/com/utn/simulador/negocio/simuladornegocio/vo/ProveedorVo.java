package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProveedorVo {

    private Long id;
    private String nombre;
    private Boolean seleccionado;
    private BigDecimal variacionCostoVariable;
    private Integer variacionCalidad;
    
    private List<ModalidadPago> modalidadesPago;

    public ProveedorVo(Proveedor proveedor, Boolean seleccionado) {

        this.id = proveedor.getId();
        this.seleccionado = seleccionado;
        this.nombre = proveedor.getNombre();
        this.variacionCostoVariable = proveedor.getVariacionCostoVariable();
        this.variacionCalidad = proveedor.getVariacionCalidad();
        this.modalidadesPago = proveedor.getModalidadPago();
    }
}
