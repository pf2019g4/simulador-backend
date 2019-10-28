package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class ProyectoBuilder extends AbstractPersistenceBuilder<Proyecto> {

    private boolean conEstadoInicial;

    private ProyectoBuilder() {
        instance = new Proyecto();
        conEstadoInicial = false;
    }

    public static ProyectoBuilder proyectoAbierto() {
        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();

        return proyectoBuilder;
    }

    public static ProyectoBuilder proyectoConProductoYEstadoInicial(Escenario escenario) {
        ProyectoBuilder proyectoBuilder = ProyectoBuilder.proyectoConEscenario(escenario);

        proyectoBuilder.conEstadoInicial = true;

        return proyectoBuilder;
    }

    public static ProyectoBuilder proyectoConEscenario(Escenario escenario) {
        ProyectoBuilder proyectoBuilder = new ProyectoBuilder();
        proyectoBuilder.instance.setEscenario(escenario);

        return proyectoBuilder;
    }

    public Proyecto build(EntityManager em) {
        if (this.instance.getEscenario() == null) {
            Escenario escenario = EscenarioBuilder.base().build(em);

            this.instance.setEscenario(escenario);
        }
        
        List<ModalidadPago> modalidadesPago = new ArrayList<>();
        modalidadesPago.add(ModalidadPagoBuilder.base(100L, 0).build(em)); //Crea modalidad de  basica (Contado)

        Proveedor proveedor = ProveedorBuilder.base(3.5D, 5, this.instance.getEscenario().getId()).build(em);
        proveedor.setModalidadPago(modalidadesPago);

        this.instance.setProveedorSeleccionado(proveedor);

        Proyecto proyecto = super.build(em);

        if (this.conEstadoInicial) {
            EstadoBuilder.inicial(proyecto).build(em);
        }
        
        List<ModalidadCobro> modalidadesCobro = new ArrayList<>();
        ModalidadCobro modalidadCobro = ModalidadCobroBuilder.base(proyecto, 100L, 0).build(em); //Crea modalidad de cobro basica (Contado)
        modalidadesCobro.add(modalidadCobro);
        this.instance.setModalidadCobro(modalidadesCobro);
        return proyecto;
    }

}
