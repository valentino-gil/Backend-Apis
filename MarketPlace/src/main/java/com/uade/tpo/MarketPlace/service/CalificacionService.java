package com.uade.tpo.MarketPlace.service;

import java.util.List;

import com.uade.tpo.MarketPlace.entity.dto.CalificacionRequest;

public interface CalificacionService {
    public CalificacionRequest agregarCalificacion(Long vendedorId, String emailComprador, int puntaje, String comentario);
    public List<CalificacionRequest> obtenerCalificacionesDeVendedor(Long vendedorId);
}
