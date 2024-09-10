package com.uade.tpo.MarketPlace.entity.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class FacturasRequest {
    private Long id;
    private double monto;
    private double descuento;
    private Date fecha;
    private Long usuario;
    

    

    public FacturasRequest(Long id, double monto, double descuento, Date fecha, Long usuario){
        this.id = id;
        this.usuario = usuario;
        this.monto = monto;
        this.descuento = descuento;
        this.fecha = fecha;
        this.usuario = usuario;
    }
    
    
}



