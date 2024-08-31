package com.uade.tpo.MarketPlace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FiltroProducto {
    private String marca;
    private String modelo;
    private Double precioMin;
    private Double precioMax;
    private Integer añoMin;
    private Integer añoMax;
}
