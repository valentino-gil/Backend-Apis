package com.uade.tpo.MarketPlace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Role;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.CarritoRequest;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.CarritoService;

@RestController
@RequestMapping("api/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @PostMapping("/")
    public ResponseEntity<CarritoRequest> agregarCarrito(@RequestBody CarritoRequest carritoRequest, 
                                                        @AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = buscarUsuario(userDetails);
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CarritoRequest carrito = carritoService.agregarCarrito(carritoRequest,usuario);
        return ResponseEntity.ok(carrito);
        }
    
    @PutMapping("/cantidad/{cantidad}")
    public ResponseEntity<CarritoRequest> modificarCantidad(@PathVariable int cantidad, @RequestBody CarritoRequest carritoRequest, 
    @AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = buscarUsuario(userDetails);
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CarritoRequest carrito = carritoService.modificarCantidad(carritoRequest,usuario,cantidad);
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarritoRequest>> obtenerCarrito(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = buscarUsuario(userDetails);
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CarritoRequest> carrito = carritoService.obtenerCarrito(usuario);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> eliminarCarrito(@PathVariable Long id, 
    @AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = buscarUsuario(userDetails);
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        carritoService.eliminarCarrito(id,usuario);
        return ResponseEntity.ok().body("Producto eliminado correctamente.");
    }

    private Usuario buscarUsuario(UserDetails userDetails){
        String usuario = userDetails.getUsername();
        return UsuarioRepository.findByMail(usuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
