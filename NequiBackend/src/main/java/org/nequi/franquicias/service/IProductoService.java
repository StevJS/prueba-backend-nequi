package org.nequi.franquicias.service;

import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;

public interface IProductoService {
    ProductoResponse createProducto(ProductoRequest request);
    DefaultResponse deleteProducto(Long sucursalId, Long productoId);
    ProductoResponse updateStockProducto(Long sucursalId, Long productoId, int nuevoStock);
    DefaultResponse updateProductoNombre(Long sucursalId, Long productoId, String nuevoNombre);
}
