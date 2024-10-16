package org.nequi.franquicias.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.exception.EntityNotFoundException;
import org.nequi.franquicias.request.FranquiciaRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.FranquiciaResponse;
import org.nequi.franquicias.service.IFranquiciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/api/v1/franquicias")
@RequiredArgsConstructor
@Validated
@Tag(name = "Franquicia", description = "API para gestionar franquicias")
public class FranquiciaController {

    private final IFranquiciaService franquiciaService;

    @PostMapping
    @Operation(summary = "Crear una nueva franquicia", description = "Crea una nueva franquicia con los datos proporcionados")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente", content = @Content(schema = @Schema(implementation = FranquiciaResponse.class))), @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<?> createFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        log.info("Creando Franquicia: {}", request);
        try {
            FranquiciaResponse franquicia = franquiciaService.createFranquicia(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(franquicia);
        } catch (Exception e) {
            log.error("Error creando Franquicia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al crear la franquicia: " + e.getMessage()));
        }
    }

    @PutMapping("/{franquiciaId}/nombre")
    @Operation(summary = "Actualizar nombre de franquicia", description = "Actualiza el nombre de una franquicia existente")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Nombre de franquicia actualizado exitosamente", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<DefaultResponse> updateFranquiciaNombre(@Parameter(description = "ID de la franquicia a actualizar") @PathVariable Long franquiciaId, @Parameter(description = "Nuevo nombre de la franquicia") @RequestParam @NotBlank String nuevoNombre) {
        log.info("Actualizando nombre de Franquicia ID: {} a: {}", franquiciaId, nuevoNombre);
        try {
            DefaultResponse response = franquiciaService.updateFranquiciaNombre(franquiciaId, nuevoNombre);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.warn("Franquicia no encontrada: {}", franquiciaId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Franquicia no encontrada."));
        } catch (Exception e) {
            log.error("Error al actualizar la franquicia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al actualizar la franquicia: " + e.getMessage()));
        }
    }
}