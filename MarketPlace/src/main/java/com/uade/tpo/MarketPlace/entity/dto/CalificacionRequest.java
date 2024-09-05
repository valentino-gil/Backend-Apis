package com.uade.tpo.MarketPlace.entity.dto;

import lombok.Data;

@Data
public class CalificacionRequest {
    private Long id;
    private int puntaje;
    private String comentario;
    private Long compradorId;
    private Long vendedorId;

    public CalificacionRequest(Long id,int puntaje,String comentario,Long compradorId,Long vendedorId){
        this.id = id;
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.compradorId = compradorId;
        this.vendedorId = vendedorId;
    }
}
