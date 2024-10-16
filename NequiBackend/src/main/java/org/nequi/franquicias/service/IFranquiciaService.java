package org.nequi.franquicias.service;

import org.nequi.franquicias.request.FranquiciaRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.FranquiciaResponse;

public interface IFranquiciaService {
    FranquiciaResponse createFranquicia(FranquiciaRequest request);

    DefaultResponse updateFranquiciaNombre(Long franquiciaId, String nuevoNombre);
}
