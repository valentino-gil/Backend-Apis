package com.uade.tpo.MarketPlace.entity.dto;


import com.uade.tpo.MarketPlace.entity.Role;

import lombok.Data;

@Data
public class UsuarioRequest {
    private int id;
    private String NombreUsuario;
    private String Nombre;
    private String Apellido;
    private String Mail;
    private String Contraseña;
    private Role role;
}