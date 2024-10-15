package com.uade.tpo.MarketPlace.entity.dto;

import lombok.Data;

@Data
public class CarritoRequest {
    private Long id;
    private Long producto;
    private int cantidad;
    private Long usuario;

    public CarritoRequest(Long id, Long producto, int cantidad, Long usuario) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.usuario = usuario;
    }

    
}
