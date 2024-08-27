package com.uade.tpo.MarketPlace.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity //declara entidad tipo tabla en bd
@Data //getters setter creados 

public class Usuario {
    public Usuario(){

    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private long id;
    @Column
    private String TipoUsuario;
    @Column
    private String NombreUsuario;
    @Column
    private String Nombre;
    @Column
    private String Apellido;
    @Column
    private String mail;
    @Column
    private String Contraseña;
    @OneToMany(mappedBy = "usuario")
    private List<Producto> productos;
}

