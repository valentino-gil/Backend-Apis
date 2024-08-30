package com.uade.tpo.MarketPlace.service;

import java.util.List;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;

public interface ProductoService {
    ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest);
    List<ProductoRequest> obtenerProductos();
    boolean eliminarProducto(Long id, String usuarioActual);
}