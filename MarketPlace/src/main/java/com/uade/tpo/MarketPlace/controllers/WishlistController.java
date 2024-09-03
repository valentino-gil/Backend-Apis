package com.uade.tpo.MarketPlace.controllers;

import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.service.WishlistService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/agregar/{productoId}")
    public ResponseEntity<ProductoRequest> agregarProductoAWishlist(@PathVariable Long productoId, Authentication authentication) {
    String emailUsuario = authentication.getName(); // El email del usuario autenticado
    
    ProductoRequest result = wishlistService.agregarProductoAWishlist(productoId, emailUsuario); // Usa la instancia de wishlistService
    return ResponseEntity.ok(result);
    
    }

    @GetMapping
    public List<ProductoRequest> obtenerWishlist(Authentication authentication) {
        String emailUsuario = authentication.getName();
        return wishlistService.obtenerWishlist(emailUsuario);
    }

    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<?> eliminarProductoDeWishlist(@PathVariable Long productoId, Authentication authentication) {
        String emailUsuario = authentication.getName();
        wishlistService.eliminarProductoDeWishlist(productoId, emailUsuario);
        return ResponseEntity.ok().body("Producto eliminado de la lista de favoritos");
    }
}
