package com.uade.tpo.MarketPlace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.ProductoService;

@RestController
@RequestMapping("api/producto")
public class ProductoController {


    

    @Autowired
    private UsuarioRepository UsuarioRepository;
    @Autowired
    private ProductoService productoService;

    // Método para registrar un producto
    @PostMapping
public ResponseEntity<ProductoRequest> registrarProducto(@RequestBody ProductoRequest productoRequest, 
                                                         @AuthenticationPrincipal UserDetails userDetails) {
    // Obtener el nombre del usuario actual desde el token
    String usuarioActual = userDetails.getUsername();
    
    
    // Obtener el usuario desde el repositorio
    Optional<Usuario> usuarioOpt = UsuarioRepository.findBymail(usuarioActual);

    if (!usuarioOpt.isPresent()) {
        // Manejar el caso en que el usuario no exista
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // O cualquier respuesta que desees
    }

    Long usuarioId = usuarioOpt.get().getId();
    if (productoRequest.getStock()==null){
        productoRequest.setStock(1);
    }
    ProductoRequest result = productoService.registrarProducto(usuarioId, productoRequest);
    return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);
}

    // Método para obtener todos los productos
    @GetMapping("/all")
    public ResponseEntity<List<ProductoRequest>> obtenerProductos() {
        List<ProductoRequest> productos = productoService.obtenerProductos();
        return ResponseEntity.ok(productos);
    }


    @GetMapping("/all/filtrar")
public ResponseEntity<List<ProductoRequest>> filtrarProductos(
    @RequestParam(required = false) String marca,
    @RequestParam(required = false) String modelo,
    @RequestParam(required = false) Double precioMin,
    @RequestParam(required = false) Double precioMax,
    @RequestParam(required = false) Integer añoMin,
    @RequestParam(required = false) Integer añoMax
) {
    // Crear un objeto FiltroProducto con los parámetros recibidos
    FiltroProducto filtro = new FiltroProducto(marca, modelo, precioMin, precioMax, añoMin, añoMax);
    
    // Obtener productos filtrados desde el servicio
    List<ProductoRequest> productosFiltrados = productoService.filtrarProductos(filtro);
    
    return ResponseEntity.ok(productosFiltrados);
}


    // Método para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id, Authentication authentication) {
        String usuarioActual = authentication.getName();
        
        
        try {
            boolean eliminado = productoService.eliminarProducto(id, usuarioActual);
            if (eliminado) {
                return ResponseEntity.ok().body("Producto eliminado correctamente");
            } else {
                return ResponseEntity.badRequest().body("No tienes permiso para eliminar este producto");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el producto: " + e.getMessage());
        }
    }

}


