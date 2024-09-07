package com.uade.tpo.MarketPlace.service;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {



        private final UsuarioRepository usuarioRepository;
        private final ProductoRepository productoRepository;


        public ProductoRequest agregarProductoAWishlist(Long productoId, String emailUsuario) {
                Usuario usuario = usuarioRepository.findByMail(emailUsuario)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Verificar si el producto ya está en la wishlist del usuario
                if (usuario.getWishlist().contains(producto)) {
                        throw new RuntimeException("El producto ya está en la wishlist");
                }

                // Si no está, agregarlo a la wishlist
                usuario.getWishlist().add(producto);
                usuarioRepository.save(usuario);

                return convertirAProductoRequest(producto);
        }


    
       public List<ProductoRequest> obtenerWishlist(String emailUsuario) {
    Usuario usuario = usuarioRepository.findByMail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    return usuario.getWishlist().stream()
            .map(producto -> new ProductoRequest(
                    producto.getId(),
                    producto.getMarca(),
                    producto.getModelo(),
                    producto.getAño(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getDescripcion(),
                    producto.getKm(),
                    obtenerImagenBase64(producto.getImagen()), // Usa el método auxiliar
                    usuario.getId()
            ))
            .collect(Collectors.toList());
}

        
        private String convertirBlobToBase64(Blob blob) throws IOException, SQLException {
        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private String obtenerImagenBase64(Blob imagen) {
        try {
            return convertirBlobToBase64(imagen);
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Manejo de errores según sea necesario
            return ""; // O algún valor predeterminado en caso de error
        }
    }
    
    
        public void eliminarProductoDeWishlist(Long productoId, String emailUsuario) {
                Usuario usuario = usuarioRepository.findByMail(emailUsuario)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
                // Verificar si el producto está en la wishlist
                if (!usuario.getWishlist().contains(producto)) {
                    throw new RuntimeException("El producto no existe en la wishlist");
                }
            
                // Eliminar el producto de la wishlist
                usuario.getWishlist().remove(producto);
                usuarioRepository.save(usuario);
            }
            

       

            private ProductoRequest convertirAProductoRequest(Producto producto) {
                // Verificar que el producto y su usuario no sean nulos
                if (producto == null) {
                    throw new IllegalArgumentException("El producto no puede ser nulo");
                }
                
                Long usuarioId = (producto.getUsuario() != null) ? producto.getUsuario().getId() : null;
                String imagenBase64 = obtenerImagenBase64(producto.getImagen()); // Utiliza el método auxiliar
            
                return new ProductoRequest(
                        producto.getId(),
                        producto.getMarca(),
                        producto.getModelo(),
                        producto.getAño(),
                        producto.getPrecio(),
                        producto.getStock(),
                        producto.getDescripcion(),
                        producto.getKm(),
                        imagenBase64, // Usa el valor convertido a Base64
                        usuarioId // Usa el ID del usuario asociado
                );
            }
            


}
