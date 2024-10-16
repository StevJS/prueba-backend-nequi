package org.nequi.franquicias.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.exception.EntityCreationException;
import org.nequi.franquicias.exception.EntityNotFoundException;
import org.nequi.franquicias.mapper.ProductoMapper;
import org.nequi.franquicias.model.entity.Producto;
import org.nequi.franquicias.model.entity.Sucursal;
import org.nequi.franquicias.repository.IProductoRepository;
import org.nequi.franquicias.repository.ISucursalRepository;
import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.service.IProductoService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final IProductoRepository repository;
    private final ISucursalRepository sucursalRepository;
    private final ProductoMapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductoResponse createProducto(ProductoRequest request) {
        try {
            log.info("Iniciando creación de producto: {}", request);

            Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                    .orElseThrow(() -> new EntityCreationException("Sucursal no encontrada",
                            new NullPointerException("No se encontró ninguna sucursal con ese ID")));

            Producto producto = mapper.requestToEntity(request);
            producto.setSucursal(sucursal);

            Producto savedProducto = repository.save(producto);
            log.info("Producto creado exitosamente con ID: {}", savedProducto.getId());
            return mapper.entityToResponse(savedProducto);
        } catch (Exception e) {
            log.error("Error al crear el producto", e);
            throw new EntityCreationException("No se pudo agregar el producto", e);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public DefaultResponse deleteProducto(Long sucursalId, Long productoId) {
        try {
            log.info("Eliminando producto con ID: {} de la sucursal con ID: {}", productoId, sucursalId);

            Sucursal sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada"));

            Producto producto = repository.findById(productoId)
                    .filter(p -> p.getSucursal().equals(sucursal))
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en la sucursal"));

            repository.delete(producto);
            log.info("Producto eliminado exitosamente con ID: {}", productoId);

            DefaultResponse response = new DefaultResponse();
            response.setMessage("El producto con ID " + productoId + " fue eliminado exitosamente de la sucursal con ID " + sucursalId + ".");
            return response;
        } catch (Exception e) {
            log.error("Error al eliminar el producto", e);
            throw new EntityCreationException("No se pudo eliminar el producto", e);
        }
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductoResponse updateStockProducto(Long sucursalId, Long productoId, int nuevoStock) {
        try {
            Sucursal sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada"));

            Producto producto = repository.findById(productoId)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            if (!producto.getSucursal().getId().equals(sucursal.getId())) {
                throw new EntityNotFoundException("El producto no pertenece a la sucursal especificada");
            }

            producto.setStock(nuevoStock);
            Producto savedProducto = repository.save(producto);
            return mapper.entityToResponse(savedProducto);
        } catch (Exception e) {
            log.error("Error al actualizar el stock del producto", e);
            throw new EntityCreationException("No se pudo actualizar el stock del producto", e);
        }
    }

    @Override
    @Transactional
    public DefaultResponse updateProductoNombre(Long sucursalId, Long productoId, String nuevoNombre) {
        try {
            Sucursal sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada"));

            Producto producto = repository.findById(productoId)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            if (!producto.getSucursal().getId().equals(sucursal.getId())) {
                throw new EntityNotFoundException("El producto no pertenece a la sucursal especificada");
            }

            producto.setNombre(nuevoNombre);
            repository.save(producto);

            DefaultResponse response = new DefaultResponse();
            response.setMessage("El nombre del producto ha sido actualizado a " + nuevoNombre + ".");
            return response;

        } catch (Exception e) {
            log.error("Error al actualizar el nombre del producto", e);
            throw new RuntimeException("No se pudo actualizar el nombre del producto", e);
        }
    }

}
