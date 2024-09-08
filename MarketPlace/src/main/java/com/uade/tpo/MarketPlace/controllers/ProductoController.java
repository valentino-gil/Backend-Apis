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
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private ProductoService productoService;

    // Método para registrar un producto

    @PostMapping
public ResponseEntity<ProductoRequest> registrarProducto(
    @RequestPart("producto") ProductoRequest productoRequest,
    @RequestPart("imagen") MultipartFile imagen,
    @AuthenticationPrincipal UserDetails userDetails) throws IOException, SQLException, java.io.IOException {

    Blob imagenBlob = convertirImagenToBlob(imagen);
    String usuarioActual = userDetails.getUsername();
    
    Optional<Usuario> usuarioOpt = UsuarioRepository.findBymail(usuarioActual);
    if (!usuarioOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long usuarioId = usuarioOpt.get().getId();
    ProductoRequest result = productoService.registrarProducto(usuarioId, productoRequest, imagenBlob);
    return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);
}

private Blob convertirImagenToBlob(MultipartFile imagen) throws SQLException, IOException, java.io.IOException {
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

    @PutMapping("/{id}")
        public ResponseEntity<?> actualizarProducto(@PathVariable Long id, 
                                                    @RequestBody ProductoRequest productoRequest, 
                                                    Authentication authentication) {
            String emailUsuario = authentication.getName();
    
            try {
                ProductoRequest productoActualizado = productoService.actualizarProducto(id, productoRequest, emailUsuario);
                return ResponseEntity.ok(productoActualizado);
            } catch (RuntimeException e) {
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


}



