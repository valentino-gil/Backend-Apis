package com.uade.tpo.MarketPlace.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.service.ProductoService;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Método para registrar un producto
    @PostMapping("/{usuarioId}")
    public ResponseEntity<ProductoRequest> registrarProducto(@PathVariable Long usuarioId, @RequestBody ProductoRequest productoRequest) {
        ProductoRequest result = productoService.registrarProducto(usuarioId, productoRequest);
        return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);
    }

    // Método para obtener todos los productos
    @GetMapping("/all")
    public ResponseEntity<List<ProductoRequest>> obtenerProductos() {
        List<ProductoRequest> productos = productoService.obtenerProductos();
        return ResponseEntity.ok(productos);
    }
}
