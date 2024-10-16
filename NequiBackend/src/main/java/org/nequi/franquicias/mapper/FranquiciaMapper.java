package org.nequi.franquicias.mapper;

import org.nequi.franquicias.model.entity.Franquicia;
import org.nequi.franquicias.model.entity.Producto;
import org.nequi.franquicias.model.entity.Sucursal;
import org.nequi.franquicias.request.FranquiciaRequest;
import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.request.SucursalRequest;
import org.nequi.franquicias.response.FranquiciaResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.response.SucursalResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FranquiciaMapper {

    public Franquicia requestToEntity(FranquiciaRequest request) {
        if (request == null) {
            return null;
        }

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(request.getNombre());

        List<Sucursal> sucursales = request.getSucursalesRequest().stream()
                .map(sucursalRequest -> getSucursal(sucursalRequest, franquicia))
                .toList();

        franquicia.setSucursales(sucursales);
        return franquicia;
    }

    private Sucursal getSucursal(SucursalRequest sucursalRequest, Franquicia franquicia) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(sucursalRequest.getNombre());
        sucursal.setFranquicia(franquicia);

        List<Producto> productos = sucursalRequest.getProductosRequest().stream()
                .map(productoRequest -> getProducto(productoRequest, sucursal))
                .toList();

        sucursal.setProductos(productos);
        return sucursal;
    }

    private Producto getProducto(ProductoRequest productoRequest, Sucursal sucursal) {
        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setStock(productoRequest.getStock());
        producto.setSucursal(sucursal);
        return producto;
    }

    public FranquiciaResponse entityToResponse(Franquicia franquicia) {
        if (franquicia == null) {
            return null;
        }

        FranquiciaResponse response = new FranquiciaResponse();
        response.setId(franquicia.getId());
        response.setNombre(franquicia.getNombre());

        List<SucursalResponse> sucursalesResponse = new ArrayList<>();
        for (Sucursal sucursal : franquicia.getSucursales()) {
            SucursalResponse sucursalResponse = getSucursalResponse(sucursal);
            sucursalesResponse.add(sucursalResponse);
        }

        response.setSucursalesResponse(sucursalesResponse);
        return response;
    }


    private SucursalResponse getSucursalResponse(Sucursal sucursal) {
        SucursalResponse sucursalResponse = new SucursalResponse();
        sucursalResponse.setId(sucursal.getId());
        sucursalResponse.setNombre(sucursal.getNombre());

        List<ProductoResponse> productosResponse = new ArrayList<>();
        for (Producto producto : sucursal.getProductos()) {
            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setId(producto.getId());
            productoResponse.setNombre(producto.getNombre());
            productoResponse.setStock(producto.getStock());
            productosResponse.add(productoResponse);
        }

        sucursalResponse.setProductosResponse(productosResponse);
        return sucursalResponse;
    }

}
