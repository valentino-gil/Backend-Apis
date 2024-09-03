package com.uade.tpo.MarketPlace.service;

import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import java.util.List;

public interface WishlistService {
    ProductoRequest agregarProductoAWishlist(Long productoId, String emailUsuario);
    List<ProductoRequest> obtenerWishlist(String emailUsuario);
    void eliminarProductoDeWishlist(Long productoId, String emailUsuario);
}