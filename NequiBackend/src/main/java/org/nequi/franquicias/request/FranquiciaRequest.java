package org.nequi.franquicias.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class FranquiciaRequest {
    @JsonIgnore
    private Long id;
    private String nombre;
    private List<SucursalRequest> sucursalesRequest;
}
