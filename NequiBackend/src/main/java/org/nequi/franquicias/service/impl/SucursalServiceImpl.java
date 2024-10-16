package org.nequi.franquicias.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.exception.EntityCreationException;
import org.nequi.franquicias.exception.EntityNotFoundException;
import org.nequi.franquicias.mapper.ProductoMapper;
import org.nequi.franquicias.mapper.SucursalMapper;
import org.nequi.franquicias.model.entity.Franquicia;
import org.nequi.franquicias.model.entity.Producto;
import org.nequi.franquicias.model.entity.Sucursal;
import org.nequi.franquicias.repository.IFranquiciaRepository;
import org.nequi.franquicias.repository.ISucursalRepository;
import org.nequi.franquicias.request.SucursalRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.response.SucursalResponse;
import org.nequi.franquicias.service.IFranquiciaService;
import org.nequi.franquicias.service.ISucursalService;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements ISucursalService {

    private final ISucursalRepository repository;
    private final IFranquiciaRepository franquiciaRepository;
    private final SucursalMapper mapper;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public SucursalResponse createSucursal(SucursalRequest request) {
        try {
            log.info("Iniciando creación de sucursal: {}", request);
            Sucursal sucursal = mapper.requestToEntity(request);

            sucursal.getProductos().forEach(producto -> producto.setSucursal(sucursal));
            Sucursal savedSucursal = repository.save(sucursal);
            log.info("Sucursal creada exitosamente con ID: {}", savedSucursal.getId());
            return mapper.entityToResponse(savedSucursal);
        } catch (Exception e) {
            log.error("Error al crear la sucursal", e);
            throw new EntityCreationException("No se pudo agregar la sucursal", e);
        }
    }

    @Override
    @Transactional
    public List<ProductoResponse> getProductosConMayorStockPorFranquicia(Long franquiciaId) {
        try {
            Optional<Franquicia> franquicia = franquiciaRepository.findById(franquiciaId);

            if (franquicia.isEmpty()) {
                throw new EntityNotFoundException("No se encontraron sucursales para la franquicia con ID " + franquiciaId);
            }

            List<ProductoResponse> responseList = new ArrayList<>();

            for (Sucursal sucursal : franquicia.get().getSucursales()) {
                List<Producto> productos = sucursal.getProductos();
                if (productos.isEmpty()) continue;

                Producto productoMayorStock = productos.stream()
                        .max(Comparator.comparingInt(Producto::getStock))
                        .orElse(null);

                if (productoMayorStock != null) {
                    ProductoResponse response = productoMapper.entityToResponse(productoMayorStock);
                    responseList.add(response);
                }
            }

            return responseList;

        } catch (Exception e) {
            log.error("Error al obtener productos con mayor stock por franquicia", e);
            throw new RuntimeException("No se pudo obtener la lista de productos", e);
        }
    }

    @Override
    @Transactional
    public DefaultResponse updateSucursalNombre(Long sucursalId, String nuevoNombre) {
        try {
            Sucursal sucursal = repository.findById(sucursalId)
                    .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada"));

            sucursal.setNombre(nuevoNombre);
            repository.save(sucursal);

            DefaultResponse response = new DefaultResponse();
            response.setMessage("El nombre de la sucursal ha sido actualizado a " + nuevoNombre + ".");
            return response;

        } catch (Exception e) {
            log.error("Error al actualizar el nombre de la sucursal", e);
            throw new RuntimeException("No se pudo actualizar el nombre de la sucursal", e);
        }
    }


}
