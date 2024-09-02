package com.uade.tpo.MarketPlace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Carrito {

    @ManyToOne
    @JoinColumn(name="producto_id")
    private Producto producto;

    @Column
    private int cantidad;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
}
