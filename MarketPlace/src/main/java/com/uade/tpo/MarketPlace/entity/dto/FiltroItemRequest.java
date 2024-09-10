package com.uade.tpo.MarketPlace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FiltroItemRequest {
    private Long id;
    private int cantidad;
    private double monto;
    private Long productoId;
}
