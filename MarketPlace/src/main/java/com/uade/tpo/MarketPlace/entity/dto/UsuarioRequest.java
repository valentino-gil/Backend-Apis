package com.uade.tpo.MarketPlace.entity.dto;


import com.uade.tpo.MarketPlace.entity.Role;
import lombok.AllArgsConstructor;
import com.uade.tpo.MarketPlace.entity.Usuario;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRequest {
    private Long id;
    private String NombreUsuario;
    private String Nombre;
    private String Apellido;
    private String Mail;
    private String Contraseña;
    private Role role;

    public UsuarioRequest(Usuario usuario) {
        this.id = usuario.getId();
        this.NombreUsuario = usuario.getNombreUsuario();
        this.Nombre = usuario.getNombre();
        this.Apellido = usuario.getApellido();
        this.Mail = usuario.getMail();
        this.role = usuario.getRole();
    }
}