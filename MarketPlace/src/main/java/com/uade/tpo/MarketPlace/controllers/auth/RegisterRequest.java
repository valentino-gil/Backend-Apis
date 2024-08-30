package com.uade.tpo.MarketPlace.controllers.auth;

import com.uade.tpo.MarketPlace.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String Nombre;
    private String Apellido;
    private String mail;
    private String Contraseña;
    private String nombreUsuario;
    private Role role;
}
