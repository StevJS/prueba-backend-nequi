package org.nequi.franquicias.response;

import lombok.Data;

import java.util.List;

@Data
public class SucursalResponse {
    private Long id;
    private String nombre;
    private List<ProductoResponse> productosResponse;
}
