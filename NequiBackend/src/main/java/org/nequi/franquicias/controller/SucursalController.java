package org.nequi.franquicias.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.request.SucursalRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.response.SucursalResponse;
import org.nequi.franquicias.service.ISucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@Validated
@Tag(name = "Sucursal", description = "API para gestionar sucursales")
public class SucursalController {

    private final ISucursalService sucursalService;

    @PostMapping("/{franquiciaId}/agregar")
    @Operation(summary = "Agregar una nueva sucursal a una franquicia", description = "Crea una nueva sucursal y la asocia a la franquicia especificada")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente", content = @Content(schema = @Schema(implementation = SucursalResponse.class))), @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<?> addSucursalToFranquicia(@Parameter(description = "ID de la franquicia") @PathVariable("franquiciaId") Long franquiciaId, @Parameter(description = "Datos de la nueva sucursal") @Valid @RequestBody SucursalRequest request) {

        log.info("Agregando nueva sucursal a la franquicia con id: {}", franquiciaId);
        request.setFranquiciaId(franquiciaId);

        try {
            SucursalResponse sucursalResponse = sucursalService.createSucursal(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(sucursalResponse);
        } catch (EntityNotFoundException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Franquicia no encontrada."));
        } catch (Exception e) {
            log.error("Error al agregar sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al agregar la sucursal: " + e.getMessage()));
        }
    }

    @GetMapping("/franquicia/{franquiciaId}/productos-mayor-stock")
    @Operation(summary = "Obtener productos con mayor stock por franquicia", description = "Retorna una lista de productos con mayor stock para una franquicia específica")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content = @Content(schema = @Schema(implementation = ProductoResponse.class))), @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<?> getProductosConMayorStockPorFranquicia(@Parameter(description = "ID de la franquicia") @PathVariable("franquiciaId") Long franquiciaId) {

        log.info("Obteniendo productos con mayor stock para la franquicia con id: {}", franquiciaId);

        try {
            List<ProductoResponse> productosConMayorStock = sucursalService.getProductosConMayorStockPorFranquicia(franquiciaId);
            return ResponseEntity.ok(productosConMayorStock);
        } catch (EntityNotFoundException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Franquicia no encontrada."));
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al obtener los productos: " + e.getMessage()));
        }
    }

    @PutMapping("/{sucursalId}/actualizar-nombre")
    @Operation(summary = "Actualizar nombre de sucursal", description = "Actualiza el nombre de una sucursal existente")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Nombre de sucursal actualizado exitosamente", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<DefaultResponse> updateSucursalNombre(@Parameter(description = "ID de la sucursal a actualizar") @PathVariable("sucursalId") Long sucursalId, @Parameter(description = "Nuevo nombre de la sucursal") @RequestParam @NotBlank String nuevoNombre) {

        log.info("Actualizando el nombre de la sucursal con id: {} a: {}", sucursalId, nuevoNombre);

        try {
            DefaultResponse response = sucursalService.updateSucursalNombre(sucursalId, nuevoNombre);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.error("Sucursal no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Sucursal no encontrada."));
        } catch (Exception e) {
            log.error("Error al actualizar el nombre de la sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al actualizar la sucursal: " + e.getMessage()));
        }
    }
}