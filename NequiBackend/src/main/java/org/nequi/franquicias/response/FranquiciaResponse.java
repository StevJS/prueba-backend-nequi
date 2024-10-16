package org.nequi.franquicias.response;

import lombok.Data;

import java.util.List;

@Data
public class FranquiciaResponse {
    private Long id;
    private String nombre;
    private List<SucursalResponse> sucursalesResponse;
}
