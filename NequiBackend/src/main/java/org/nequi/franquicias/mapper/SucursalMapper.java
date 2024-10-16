package org.nequi.franquicias.mapper;

import lombok.RequiredArgsConstructor;
import org.nequi.franquicias.model.entity.Franquicia;
import org.nequi.franquicias.model.entity.Producto;
import org.nequi.franquicias.model.entity.Sucursal;
import org.nequi.franquicias.repository.ISucursalRepository;
import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.request.SucursalRequest;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.response.SucursalResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SucursalMapper {

    public Sucursal requestToEntity(SucursalRequest request) {
        if (request == null) {
            return null;
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(request.getNombre());

        Franquicia franquicia = new Franquicia();
        franquicia.setId(request.getFranquiciaId());
        sucursal.setFranquicia(franquicia);

        List<Producto> productos = request.getProductosRequest() != null
                ? request.getProductosRequest().stream()
                .map(this::getProducto)
                .toList()
                : List.of();

        sucursal.setProductos(productos);

        return sucursal;
    }

    private Producto getProducto(ProductoRequest productoRequest) {
        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setStock(productoRequest.getStock());
        return producto;
    }

    public SucursalResponse entityToResponse(Sucursal sucursal) {
        if (sucursal == null) {
            return null;
        }

        SucursalResponse response = new SucursalResponse();
        response.setId(sucursal.getId());
        response.setNombre(sucursal.getNombre());

        List<ProductoResponse> productosResponse = sucursal.getProductos().stream()
                .map(this::getProductoResponse)
                .toList();

        response.setProductosResponse(productosResponse);
        return response;
    }

    private ProductoResponse getProductoResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setStock(producto.getStock());
        return response;
    }
}