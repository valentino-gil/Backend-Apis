package com.uade.tpo.MarketPlace.service;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public ProductoRequest agregarProductoAWishlist(Long productoId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByMail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        usuario.getWishlist().add(producto);
        usuarioRepository.save(usuario);
        return convertirAProductoRequest(producto);

        
    }

    @Override
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
                        usuario.getId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarProductoDeWishlist(Long productoId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByMail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        usuario.getWishlist().remove(producto);
        usuarioRepository.save(usuario);
    }

    private ProductoRequest convertirAProductoRequest(Producto producto) {
        return new ProductoRequest(
                producto.getId(),
                producto.getMarca(),
                producto.getModelo(),
                producto.getAño(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getUsuario().getId()); // Devolvemos el DTO con usuarioId
    }

}
