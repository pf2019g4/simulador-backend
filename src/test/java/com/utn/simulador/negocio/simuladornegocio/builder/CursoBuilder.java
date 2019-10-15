package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;

public class CursoBuilder extends AbstractPersistenceBuilder<Curso> {

    private CursoBuilder() {
        instance = new Curso();
    }

    public static CursoBuilder base(String nombre, String clave) {
        CursoBuilder cursoBuilder = new CursoBuilder();
        cursoBuilder.instance.setNombre(nombre);
        cursoBuilder.instance.setClave(clave);

        return cursoBuilder;
    }

}
