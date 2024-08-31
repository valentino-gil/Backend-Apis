package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificamos si el auto ya fue registrado por el usuario
        if (productoRepository.existsByMarcaAndModeloAndUsuarioId(
                productoRequest.getMarca(), productoRequest.getModelo(), usuarioId, productoRequest.getAño())) {
            throw new IllegalArgumentException("El usuario ya tiene registrado un auto de esta marca y modelo");
        }

        // Creamos el auto y lo asignamos al usuario
        Producto producto = new Producto();
        producto.setMarca(productoRequest.getMarca());
        producto.setModelo(productoRequest.getModelo());
        producto.setAño(productoRequest.getAño());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setUsuario(usuario);
        producto.setStock(productoRequest.getStock());
        productoRepository.save(producto);

        return convertirAProductoRequest(producto);
                
    }

    
    public List<ProductoRequest> obtenerProductos() {
        List<Producto> productos = productoRepository.findAll();
    return productos.stream()
        .map(producto -> convertirAProductoRequest(producto))
        .collect(Collectors.toList());
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
    public boolean eliminarProducto(Long id, String usuarioActual) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Usuario usuario = usuarioRepository.findBymail(usuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verifica si el usuario actual es el propietario del producto
        if (producto.getUsuario().getId() == usuario.getId()) {
            productoRepository.delete(producto);
            return true;
        }
        
        return false;
    }
    
    

}

