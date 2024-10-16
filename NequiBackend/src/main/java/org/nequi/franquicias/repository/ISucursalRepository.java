package org.nequi.franquicias.repository;

import org.nequi.franquicias.model.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISucursalRepository extends JpaRepository<Sucursal, Long> {
}
