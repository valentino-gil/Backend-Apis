package com.uade.tpo.MarketPlace.entity.dto;


import lombok.Data;

@Data
public class UsuarioRequest {
    private int id;
    private String TipoUsuario;
    private String NombreUsuario;
}