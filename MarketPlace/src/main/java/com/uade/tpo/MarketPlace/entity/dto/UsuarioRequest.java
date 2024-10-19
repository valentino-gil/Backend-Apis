package com.uade.tpo.MarketPlace.entity.dto;


import com.uade.tpo.MarketPlace.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRequest {
    private int id;
    private String NombreUsuario;
    private String Nombre;
    private String Apellido;
    private String Mail;
    private String Contraseña;
    private Role role;
    
    public UsuarioRequest(String nombreUsuario, String nombre, String apellido, String mail) {
        NombreUsuario = nombreUsuario;
        Nombre = nombre;
        Apellido = apellido;
        Mail = mail;
    }

    
}