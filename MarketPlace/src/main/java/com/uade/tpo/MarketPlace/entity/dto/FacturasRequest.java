package com.uade.tpo.MarketPlace.entity.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class FacturasRequest {
    private Long id;
    private int monto;
    private Date fecha;
    private Long usuario_id;


    public FacturasRequest(Long id, int monto, Date fecha, Long usuario_id) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.usuario_id = usuario_id;
    }

}
