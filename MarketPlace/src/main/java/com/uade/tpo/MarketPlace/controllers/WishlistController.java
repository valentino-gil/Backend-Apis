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


    @PostMapping("/{productoId}")
    public ResponseEntity<?> agregarProductoAWishlist(@PathVariable Long productoId, Authentication authentication) {
        String emailUsuario = authentication.getName(); // El email del usuario autenticado
    
        try {
            ProductoRequest result = wishlistService.agregarProductoAWishlist(productoId, emailUsuario); // Usa la instancia de wishlistService
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // Si ocurre una excepción, devolver un código de estado 400 (Bad Request) con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    


    @GetMapping
    public List<ProductoRequest> obtenerWishlist(Authentication authentication) {
        String emailUsuario = authentication.getName();
        return wishlistService.obtenerWishlist(emailUsuario);
    }


    @DeleteMapping("/{productoId}")
public ResponseEntity<?> eliminarProductoDeWishlist(@PathVariable Long productoId, Authentication authentication) {
    String emailUsuario = authentication.getName();

    try {
        wishlistService.eliminarProductoDeWishlist(productoId, emailUsuario);
        return ResponseEntity.ok().body("Producto eliminado de la lista de favoritos");
    } catch (RuntimeException e) {
        // Manejar la excepción y devolver el mensaje de error
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}

    


