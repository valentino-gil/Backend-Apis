package com.uade.tpo.MarketPlace.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data //getters setter creados 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity //declara entidad tipo tabla en bd
public class Usuario implements UserDetails{
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "vendedor")
    private List<Calificacion> calificacionesRecibidas;

    @OneToMany(mappedBy = "comprador")
    private List<Calificacion> calificacionesRealizadas;

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public String getPassword() {
        return Contraseña;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

