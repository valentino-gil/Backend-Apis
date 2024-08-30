package com.uade.tpo.MarketPlace.entity.dto;

import lombok.Data;

@Data
public class ProductoRequest {
    private Long id;
    private String marca;
    private String modelo;
    private int año;
    private double precio;
    private Long usuarioId; 

    
    public ProductoRequest(Long id, String marca, String modelo, int año, double precio, Long usuarioId) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.precio = precio;
        this.usuarioId = usuarioId;
    }
}
