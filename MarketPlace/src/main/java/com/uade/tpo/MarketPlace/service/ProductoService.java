package com.uade.tpo.MarketPlace.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.dto.FiltroProducto;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;

import io.jsonwebtoken.io.IOException;

public interface ProductoService {
    public ProductoRequest registrarProducto(Long usuarioId, ProductoRequest productoRequest, Blob imagenBlob) throws IOException, SQLException;
    public List<ProductoRequest> obtenerProductos();
    boolean eliminarProducto(Long id, String usuarioActual);
    public List<ProductoRequest> filtrarProductos(FiltroProducto filtro);
    public ProductoRequest actualizarProducto(Long id, ProductoRequest productoRequest, String emailUsuario);
}