package org.nequi.franquicias.repository;

import org.nequi.franquicias.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
