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
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nequi.franquicias.request.ProductoRequest;
import org.nequi.franquicias.response.DefaultResponse;
import org.nequi.franquicias.response.ProductoResponse;
import org.nequi.franquicias.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Producto", description = "API para gestionar productos en sucursales")
public class ProductoController {

    private final IProductoService productoService;

    @PostMapping("/{sucursalId}/agregar")
    @Operation(summary = "Agregar producto a sucursal", description = "Agrega un nuevo producto a una sucursal específica")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Producto agregado exitosamente", content = @Content(schema = @Schema(implementation = ProductoResponse.class))), @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<?> addProductoToSucursal(@Parameter(description = "ID de la sucursal") @PathVariable Long sucursalId, @Valid @RequestBody ProductoRequest request) {
        log.info("Agregando nuevo producto a la sucursal con id: {}", sucursalId);
        request.setSucursalId(sucursalId);
        try {
            ProductoResponse productoResponse = productoService.createProducto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoResponse);
        } catch (EntityNotFoundException e) {
            log.warn("Sucursal no encontrada: {}", sucursalId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Sucursal no encontrada"));
        } catch (Exception e) {
            log.error("Error al agregar producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al agregar producto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{sucursalId}/eliminar/{productoId}")
    @Operation(summary = "Eliminar producto de sucursal", description = "Elimina un producto específico de una sucursal")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrados", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<DefaultResponse> deleteProductoFromSucursal(@Parameter(description = "ID de la sucursal") @PathVariable Long sucursalId, @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        log.info("Eliminando producto con id: {} de la sucursal con id: {}", productoId, sucursalId);
        try {
            DefaultResponse response = productoService.deleteProducto(sucursalId, productoId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.warn("Producto o sucursal no encontrados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Producto o sucursal no encontrados"));
        } catch (Exception e) {
            log.error("Error al eliminar producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al eliminar producto: " + e.getMessage()));
        }
    }

    @PutMapping("/{sucursalId}/actualizar-stock/{productoId}")
    @Operation(summary = "Actualizar stock de producto", description = "Actualiza el stock de un producto específico en una sucursal")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente", content = @Content(schema = @Schema(implementation = ProductoResponse.class))), @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrados", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<?> updateProductoStock(@Parameter(description = "ID de la sucursal") @PathVariable Long sucursalId, @Parameter(description = "ID del producto") @PathVariable Long productoId, @Parameter(description = "Nuevo stock del producto") @RequestParam @Min(0) int nuevoStock) {
        log.info("Actualizando stock del producto con id: {} en la sucursal con id: {}. Nuevo stock: {}", productoId, sucursalId, nuevoStock);
        try {
            ProductoResponse productoResponse = productoService.updateStockProducto(sucursalId, productoId, nuevoStock);
            return ResponseEntity.ok(productoResponse);
        } catch (EntityNotFoundException e) {
            log.warn("Producto o sucursal no encontrados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Producto o sucursal no encontrados"));
        } catch (Exception e) {
            log.error("Error al actualizar stock", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al actualizar stock: " + e.getMessage()));
        }
    }

    @PutMapping("/{sucursalId}/actualizar-nombre/{productoId}")
    @Operation(summary = "Actualizar nombre de producto", description = "Actualiza el nombre de un producto específico en una sucursal")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrados", content = @Content(schema = @Schema(implementation = DefaultResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = DefaultResponse.class)))})
    public ResponseEntity<DefaultResponse> updateProductoNombre(@Parameter(description = "ID de la sucursal") @PathVariable Long sucursalId, @Parameter(description = "ID del producto") @PathVariable Long productoId, @Parameter(description = "Nuevo nombre del producto") @RequestParam String nuevoNombre) {
        log.info("Actualizando el nombre del producto con id: {} en la sucursal con id: {}", productoId, sucursalId);
        try {
            DefaultResponse response = productoService.updateProductoNombre(sucursalId, productoId, nuevoNombre);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.warn("Producto o sucursal no encontrados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse("Producto o sucursal no encontrados"));
        } catch (Exception e) {
            log.error("Error al actualizar el nombre del producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse("Error al actualizar el producto: " + e.getMessage()));
        }
    }
}