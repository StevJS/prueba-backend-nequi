package org.nequi.franquicias.repository;

import org.nequi.franquicias.model.entity.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFranquiciaRepository extends JpaRepository<Franquicia, Long> {
}
