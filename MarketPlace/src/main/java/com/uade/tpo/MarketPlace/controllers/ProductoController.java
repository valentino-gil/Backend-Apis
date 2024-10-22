package com.uade.tpo.MarketPlace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.exceptions.ProductoDuplicateException;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.ProductoService;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoService productoService;

    // Método para registrar un producto
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> registrarProducto(
        @RequestPart("producto") ProductoRequest productoRequest,
        @RequestPart("imagen") MultipartFile imagen,
        @AuthenticationPrincipal UserDetails userDetails) {

        try {
            Blob imagenBlob = convertirImagenToBlob(imagen);
            String usuarioActual = userDetails.getUsername();

            Optional<Usuario> usuarioOpt = usuarioRepository.findBymail(usuarioActual);
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado.");
            }

            Long usuarioId = usuarioOpt.get().getId();
            ProductoRequest result = productoService.registrarProducto(usuarioId, productoRequest, imagenBlob);
            return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);

        } catch (IllegalArgumentException e) {
            logger.error("Error en el registro del producto: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error en la solicitud: " + e.getMessage());
        } catch (ProductoDuplicateException e) {
            logger.error("Producto duplicado: {}", e.getMessage());
            return ResponseEntity.badRequest().body("El producto ya existe.");
        } catch (IOException | SQLException e) {
            logger.error("Error en el registro del producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen o datos.");
        }
    }

    private Blob convertirImagenToBlob(MultipartFile imagen) throws SQLException, IOException {
        if (imagen != null && !imagen.isEmpty()) {
            byte[] imagenBytes = imagen.getBytes();
            return new SerialBlob(imagenBytes);
        }
        return null;
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
        FiltroProducto filtro = new FiltroProducto(marca, modelo, precioMin, precioMax, añoMin, añoMax);
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
                return ResponseEntity.ok().body("Producto eliminado correctamente.");
            } else {
                return ResponseEntity.badRequest().body("No tienes permiso para eliminar este producto.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, 
                                                @RequestBody ProductoRequest productoRequest, 
                                                Authentication authentication) {
        String emailUsuario = authentication.getName();

        try {
            ProductoRequest productoActualizado = productoService.actualizarProducto(id, productoRequest, emailUsuario);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el producto: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/{productoId}/imagen")
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long productoId) {
        try {
            byte[] imagenBytes = productoService.obtenerImagenProducto(productoId);

            if (imagenBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Usa MediaType para el tipo de contenido
                        .body(imagenBytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la imagen del producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all/{id}")
public ResponseEntity<ProductoRequest> getProductoById(@PathVariable Long id) {
    Optional<ProductoRequest> productoRequest = productoService.getProductoById(id);

    // Si el producto fue encontrado, devolver el ProductoRequest
    if (productoRequest.isPresent()) {
        return ResponseEntity.ok(productoRequest.get());
    } else {
        // Si no fue encontrado, devolver un 404
        return ResponseEntity.notFound().build();
    }
}

    @GetMapping("/all/all")
    public ResponseEntity<List<ProductoRequest>> getAllProductos(){
        List<ProductoRequest> productos = productoService.obtenerTodosProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/misVehiculos")
    public ResponseEntity<List<ProductoRequest>> obtenerMisProductos(@AuthenticationPrincipal UserDetails userDetails) {
        // Obtener el email del usuario autenticado
        String emailUsuario = userDetails.getUsername();

        // Obtener la lista de productos del usuario
        List<ProductoRequest> misProductos = productoService.obtenerProductosPorUsuario(emailUsuario);

        return ResponseEntity.ok(misProductos);
    }

}