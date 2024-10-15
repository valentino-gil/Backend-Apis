package com.uade.tpo.MarketPlace.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Carrito;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.CarritoRequest;
import com.uade.tpo.MarketPlace.repository.CarritoRepository;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public CarritoRequest agregarCarrito(CarritoRequest carritoRequest, Usuario usuario){
        Carrito carrito = new Carrito();
        carrito.setCantidad(carritoRequest.getCantidad());
        Producto producto = productoRepository.findById(carritoRequest.getProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        carrito.setProducto(producto);
        carrito.setUsuario(usuario);
        carritoRepository.save(carrito);
        return convertirACarritoRequest(carrito);
    }

    public CarritoRequest modificarCantidad(CarritoRequest carritoRequest, Usuario usuario, int cantidad){
        Carrito carrito = carritoRepository.findByProducto(carritoRequest.getProducto(),usuario.getId())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        carrito.setCantidad(cantidad);
        carritoRepository.save(carrito);
        return convertirACarritoRequest(carrito);
    }

    public void eliminarCarrito(Long id, Usuario usuario){
        Carrito carrito = carritoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (carrito.getUsuario().getId().equals(usuario.getId()))
            carritoRepository.delete(carrito);
        else{
            throw new RuntimeException("No esta en el carrito del usuario");
        }
    }

    public List<CarritoRequest> obtenerCarrito(Usuario usuario){
        List<Carrito> items = carritoRepository.findByUsuarioId(usuario.getId());
        return items.stream()
        .map(c -> convertirACarritoRequest(c))
        .collect(Collectors.toList());
    }

    private CarritoRequest convertirACarritoRequest(Carrito carrito){
        return new CarritoRequest(
            carrito.getId(),
            carrito.getProducto().getId(),
            carrito.getCantidad(),
            carrito.getUsuario().getId()
        );
    }
}
