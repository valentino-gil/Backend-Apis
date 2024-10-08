package com.uade.tpo.MarketPlace.entity;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Producto {
    public Producto(){
        
    }
    //dada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String marca;

    @Column
    private String modelo;

    @Column
    private int año;

    @Column
    private double precio;

    @Column(nullable = false)
    private Integer stock = 1;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column
    private String descripcion;
    
    @Column
    private double km;

    @Column
    private Blob imagen;
    
    @OneToMany(mappedBy = "producto")
    private List<ItemsFactura> items;
}
