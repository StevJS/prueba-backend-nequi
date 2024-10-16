package org.nequi.franquicias.service;

import org.nequi.franquicias.request.SucursalRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.response.SucursalResponse;

import java.util.List;

public interface ISucursalService {
    SucursalResponse createSucursal(SucursalRequest request);

    List<ProductoResponse> getProductosConMayorStockPorFranquicia(Long franquiciaId);

    DefaultResponse updateSucursalNombre(Long sucursalId, String nuevoNombre);
}
