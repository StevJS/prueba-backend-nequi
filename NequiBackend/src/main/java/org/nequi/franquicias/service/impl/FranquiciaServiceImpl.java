package org.nequi.franquicias.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.exception.EntityCreationException;
import org.nequi.franquicias.exception.EntityNotFoundException;
import org.nequi.franquicias.mapper.FranquiciaMapper;
import org.nequi.franquicias.model.entity.Franquicia;
import org.nequi.franquicias.repository.IFranquiciaRepository;
import org.nequi.franquicias.request.FranquiciaRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.FranquiciaResponse;
import org.nequi.franquicias.service.IFranquiciaService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements IFranquiciaService {

    private final IFranquiciaRepository repository;
    private final FranquiciaMapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public FranquiciaResponse createFranquicia(FranquiciaRequest request) {
        try {
            log.info("Iniciando creación de franquicia: {}", request);
            Franquicia franquicia = mapper.requestToEntity(request);

            franquicia.getSucursales().forEach(sucursal -> {
                sucursal.setFranquicia(franquicia);
                sucursal.getProductos().forEach(producto -> producto.setSucursal(sucursal));
            });

            Franquicia savedFranquicia = repository.save(franquicia);
            log.info("Franquicia creada exitosamente con ID: {}", savedFranquicia.getId());
            FranquiciaResponse response = mapper.entityToResponse(savedFranquicia);
            return response;
        } catch (Exception e) {
            log.error("Error al crear la franquicia", e);
            throw new EntityCreationException("No se pudo crear la franquicia", e);
        }
    }

    @Override
    @Transactional
    public DefaultResponse updateFranquiciaNombre(Long franquiciaId, String nuevoNombre) {
        try {
            Franquicia franquicia = repository.findById(franquiciaId)
                    .orElseThrow(() -> new EntityNotFoundException("Franquicia no encontrada"));

            franquicia.setNombre(nuevoNombre);
            repository.save(franquicia);

            DefaultResponse response = new DefaultResponse();
            response.setMessage("El nombre de la franquicia ha sido actualizado a " + nuevoNombre + ".");
            return response;

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Franquicia no encontrada.");
        } catch (Exception e) {
            log.error("Error al actualizar el nombre de la franquicia", e);
            throw new RuntimeException("No se pudo actualizar el nombre de la franquicia", e);
        }
    }

}
