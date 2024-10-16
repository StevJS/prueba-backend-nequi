package org.nequi.franquicias.response;

import lombok.Data;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private int stock;
}
