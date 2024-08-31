package com.uade.tpo.MarketPlace.entity.dto;

import lombok.Data;

@Data
public class FiltroProducto {
    private String marca;
    private String modelo;
    private Double precioMin;
    private Double precioMax;
    private Integer añoMin;
    private Integer añoMax;
}
