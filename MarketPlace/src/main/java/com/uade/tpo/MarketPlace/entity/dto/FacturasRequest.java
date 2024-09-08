package com.uade.tpo.MarketPlace.entity.dto;

import java.sql.Date;
import java.util.List;

import com.uade.tpo.MarketPlace.entity.Usuario;

import lombok.Data;

@Data
public class FacturasRequest {
    private Long id;
    private double monto;
    private Date fecha;
    private Usuario usuario;
    private List<ItemRequest> items;

    public FacturasRequest(List<ItemRequest> items){
        this.items = items;
    }

    public FacturasRequest(Long id, double monto, Date fecha, Usuario usuario){
        this.usuario = usuario;
        this.monto = monto;
        this.fecha = fecha;
        this.usuario = usuario;
    }
    
    @Data
    public static class ItemRequest {
        private Long productoId;
        private int cantidad;
        private double monto;
        private Long Id;
        private Long facturaId;

        public ItemRequest(Long productoId, int cantidad){
            this.productoId = productoId;
            this.cantidad = cantidad;
        }
    }


}
