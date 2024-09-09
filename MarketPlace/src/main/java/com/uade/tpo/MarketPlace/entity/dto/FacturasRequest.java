package com.uade.tpo.MarketPlace.entity.dto;

import java.sql.Date;
import java.util.List;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.ItemRequest;
import lombok.Data;

@Data
public class FacturasRequest {
    private Long id;
    private double monto;
    private double descuento;
    private Date fecha;
    private Usuario usuario;
    private List<ItemRequest> items;

    public FacturasRequest(List<ItemRequest> items){
        this.items = items;
    }

    public FacturasRequest(Long id, double monto, double descuento, Date fecha, Usuario usuario){
        this.usuario = usuario;
        this.monto = monto;
        this.descuento = descuento;
        this.fecha = fecha;
        this.usuario = usuario;
    }
    
    
}



