package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModalidadPagoRepository extends JpaRepository<ModalidadPago, Long> {

    List<ModalidadPago> findByProveedorId(Long idProveedor);
}
