package com.uade.tpo.MarketPlace.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class ProductoServiceImpl implements ProductoService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest, Blob imagenBlob) throws IOException, SQLException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    
        Producto producto = new Producto();
        producto.setMarca(productoRequest.getMarca());
        producto.setModelo(productoRequest.getModelo());
        producto.setAño(productoRequest.getAño());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setUsuario(usuario);
        producto.setStock(productoRequest.getStock());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setKm(productoRequest.getKm());
    
        // Asignar Blob si está disponible
        if (imagenBlob != null && imagenBlob.length() > 0) {
            producto.setImagen(imagenBlob);
        }
    
        productoRepository.save(producto);
        return convertirAProductoRequest(producto);
    }
    
    
    

    
    public List<ProductoRequest> obtenerProductos() {
        List<Producto> productos = productoRepository.findAllOrderByVendedorNivelDesc();
        return productos.stream()
            .map(producto -> convertirAProductoRequest(producto))
            .collect(Collectors.toList());
    }
    

    private ProductoRequest convertirAProductoRequest(Producto producto) {
        ProductoRequest productoRequest = new ProductoRequest();
        productoRequest.setId(producto.getId());
        productoRequest.setMarca(producto.getMarca());
        productoRequest.setModelo(producto.getModelo());
        productoRequest.setAño(producto.getAño());
        productoRequest.setPrecio(producto.getPrecio());
        productoRequest.setStock(producto.getStock());
        productoRequest.setDescripcion(producto.getDescripcion());
        productoRequest.setKm(producto.getKm());
        productoRequest.setUsuarioId(producto.getUsuario().getId());
    
        try {
            String imagenBase64 = convertirBlobToBase64(producto.getImagen());
            productoRequest.setImagen(imagenBase64);
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Manejo de errores según sea necesario
        }
    
        return productoRequest;
    }
    
    


    private String convertirBlobToBase64(Blob blob) throws IOException, SQLException {
        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    public boolean eliminarProducto(Long id, String usuarioActual) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Usuario usuario = usuarioRepository.findBymail(usuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        // Verifica si el usuario actual es el propietario del producto
        if (producto.getUsuario().getId().equals(usuario.getId())) {
            productoRepository.delete(producto);
            return true;
        }
        System.out.println("Usuario Actual: " + usuarioActual);
        System.out.println("Usuario desde la base de datos: " + usuario.getId());
        System.out.println("ID del Producto: " + producto.getUsuario().getId());

        
        return false;
    }
    

    public List<ProductoRequest> filtrarProductos(FiltroProducto filtro) {
        // Obtener la lista de entidades Producto filtradas
        List<Producto> productos = productoRepository.filtrarProductos(
            filtro.getMarca(),
            filtro.getModelo(),
            filtro.getPrecioMin(),
            filtro.getPrecioMax(),
            filtro.getAñoMin(),
            filtro.getAñoMax()
        );
        
        // Convertir la lista de productos en una lista de ProductoRequest
        return productos.stream()
            .map(this::convertirAProductoRequest)
            .collect(Collectors.toList());
    }
    
    public ProductoRequest actualizarProducto(Long id, ProductoRequest productoRequest, String emailUsuario) {
        // Buscar el producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    
        // Buscar el usuario autenticado
        Usuario usuario = usuarioRepository.findBymail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        // Verificar si el usuario es el propietario del producto
        if (!producto.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para actualizar este producto");
        }
    
        // Actualizar solo los atributos que no sean nulos
        if (productoRequest.getMarca() != null) {
            producto.setMarca(productoRequest.getMarca());
        }
        if (productoRequest.getModelo() != null) {
            producto.setModelo(productoRequest.getModelo());
        }
        if (productoRequest.getAño() != 0) {
            producto.setAño(productoRequest.getAño());
        }
        if (productoRequest.getPrecio() != 0.0) {
            producto.setPrecio(productoRequest.getPrecio());
        }
        if (productoRequest.getStock() != null) {
            producto.setStock(productoRequest.getStock());
        }
    
        // Guardar los cambios en la base de datos
        productoRepository.save(producto);
    
        return convertirAProductoRequest(producto);
    }
    
    
    

}
