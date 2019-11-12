package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.EmpresaCompetidora;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;

public class EmpresaCompetidorasBuilder extends AbstractPersistenceBuilder<EmpresaCompetidora> {

    private EmpresaCompetidorasBuilder() {
        instance = new EmpresaCompetidora();
    }

    public static EmpresaCompetidorasBuilder todosMercadosAl95(Escenario escenario) {
        EmpresaCompetidorasBuilder empresaCompetidoraBuilder = new EmpresaCompetidorasBuilder();
        empresaCompetidoraBuilder.instance.setNombre("ponderaciones al 95");
        empresaCompetidoraBuilder.instance.setEscenarioId(escenario.getId());
        empresaCompetidoraBuilder.instance.setAlto(95);
        empresaCompetidoraBuilder.instance.setMedio(95);
        empresaCompetidoraBuilder.instance.setBajo(95);
        return empresaCompetidoraBuilder;
    }
}
