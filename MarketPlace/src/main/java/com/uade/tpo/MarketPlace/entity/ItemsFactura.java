package com.uade.tpo.MarketPlace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ItemsFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int cantidad;

    @Column
    private double monto;

    @ManyToOne
    @JoinColumn (name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Facturas factura;
}
