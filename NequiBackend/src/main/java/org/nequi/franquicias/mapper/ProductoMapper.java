package org.nequi.franquicias.mapper;

import org.nequi.franquicias.model.entity.Producto;
import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.response.ProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestToEntity(ProductoRequest request) {
        if (request == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setStock(request.getStock());

        return producto;
    }

    public ProductoResponse entityToResponse(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setStock(producto.getStock());

        return response;
    }
}
