package com.uade.tpo.MarketPlace.entity.dto;

import lombok.Data;

@Data
public class ItemsFacturaRequest {
    private Long id;
    private int cantidad;
    private double monto;
    private Long producto_id;
    private Long factura_id;


    public ItemsFacturaRequest(Long id, int cantidad, double monto, Long producto_id, Long factura_id) {
        this.id = id;
        this.cantidad = cantidad;
        this.monto = monto;
        this.producto_id = producto_id;
        this.factura_id = factura_id;
    }

}
