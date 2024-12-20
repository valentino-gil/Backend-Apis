package com.uade.tpo.MarketPlace.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.exceptions.ProductoDuplicateException;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import io.jsonwebtoken.io.IOException;

@Service
public class ProductoServiceImpl implements ProductoService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest, Blob imagenBlob) throws IOException, SQLException, ProductoDuplicateException {
    // Verificar que el stock sea válido
    if (productoRequest.getStock() <= 0) {
        throw new IllegalArgumentException("Stock tiene que ser un número válido");
    }
    if (productoRequest.getAño() < 1000 || productoRequest.getAño() > 2026) {
        throw new IllegalArgumentException("Año Invalido");
    }
    // Buscar el usuario
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    // Verificar si el producto ya existe
    boolean productoExiste = productoRepository.existsByMarcaAndModeloAndUsuarioId(
            productoRequest.getMarca(),
            productoRequest.getModelo(),
            usuarioId,
            productoRequest.getAño()
    );

    if (productoExiste) {
        throw new ProductoDuplicateException("El producto ya se encuentra registrado.");
    }

    // Crear nuevo producto
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

    // Guardar el producto
    productoRepository.save(producto);

    // Convertir y devolver ProductoRequest
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
        productoRequest.setImagen("http://localhost:8080/api/producto/all/" + producto.getId() + "/imagen");
        productoRequest.setUsuarioId(producto.getUsuario().getId());

    
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
    
    public byte[] obtenerImagenProducto(Long productoId) throws SQLException {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            Blob imagenBlob = producto.getImagen();

            if (imagenBlob != null) {
                return imagenBlob.getBytes(1, (int) imagenBlob.length());
            }
        }

        return null; 
    }

    public Optional<ProductoRequest> getProductoById(Long id) {
        // Buscar el producto en el repositorio
        Optional<Producto> productoOpt = productoRepository.findById(id);
    
        // Si el producto está presente, convertirlo a ProductoRequest
        return productoOpt.map(this::convertirAProductoRequest);
    }

    public List<ProductoRequest> obtenerTodosProductos(){
        List<Producto> productos = productoRepository.findAllProductos();
        return productos.stream()
            .map(producto -> convertirAProductoRequest(producto))
            .collect(Collectors.toList());
    }

    public List<ProductoRequest> obtenerProductosPorUsuario(String emailUsuario) {
        // Buscar el usuario por su email
        Usuario usuario = usuarioRepository.findBymail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener los productos del usuario
        List<Producto> productos = productoRepository.findByUsuarioId(usuario.getId());

        // Convertir la lista de Producto a ProductoRequest
        return productos.stream()
                .map(this::convertirAProductoRequest)
                .collect(Collectors.toList());
    }

    public List<ProductoRequest> buscarProductosPorMarcaOModelo(String query) {
        // Filtrar por marca o modelo (puedes personalizar esta lógica)
        return productoRepository.findAll().stream()
                .filter(producto -> 
                    producto.getMarca().toLowerCase().contains(query.toLowerCase()) ||
                    producto.getModelo().toLowerCase().contains(query.toLowerCase()))
                .map(this::convertirAProductoRequest)
                .collect(Collectors.toList());
    }
    
    
}
