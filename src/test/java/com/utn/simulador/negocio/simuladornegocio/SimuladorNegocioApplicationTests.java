package com.utn.simulador.negocio.simuladornegocio;

import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SimuladorNegocioApplicationTests {

    @Autowired
    protected EntityManager em;

    @Test
    public void contextLoads() {
    }

}
