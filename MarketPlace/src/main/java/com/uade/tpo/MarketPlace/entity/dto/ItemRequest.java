package com.uade.tpo.MarketPlace.entity.dto;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.Producto;

import lombok.Data;

@Data
public class ItemRequest {
   
    private Long productoId;
    private int cantidad;
    private double monto;
    private Long Id;
    private Facturas facturaId;

    public ItemRequest(){
            
    }

    public ItemRequest(Long productoId, int cantidad){
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public ItemRequest(Long productoId, int cantidad, double monto, Long id, Facturas facturaId){
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.monto = monto;
        this.Id = id;
        this.facturaId = facturaId;
        }
    
}


