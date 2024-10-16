package org.nequi.franquicias.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ProductoRequest {
    @JsonIgnore
    private Long id;

    private String nombre;
    private int stock;

    @JsonIgnore
    private Long sucursalId;
}
