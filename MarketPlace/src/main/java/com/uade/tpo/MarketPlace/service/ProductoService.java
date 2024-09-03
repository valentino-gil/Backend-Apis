package com.uade.tpo.MarketPlace.service;

import java.util.List;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;

public interface ProductoService {
    public ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest);
    public List<ProductoRequest> obtenerProductos();
    boolean eliminarProducto(Long id, String usuarioActual);
    public List<ProductoRequest> filtrarProductos(FiltroProducto filtro);
}