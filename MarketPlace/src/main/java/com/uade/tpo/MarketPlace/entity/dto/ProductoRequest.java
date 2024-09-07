package com.uade.tpo.MarketPlace.entity.dto;

import java.sql.Blob;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductoRequest {
    private Long id;
    private String marca;
    private String modelo;
    private int año;
    private double precio;
    private Integer stock;
    private String descripcion;
    private double km;
    private String imagen;
    private Long usuarioId;
     
    public ProductoRequest() {
    }
    

    
    public ProductoRequest(Long id, String marca, String modelo, int año, double precio, Integer stock, String descripcion,double km,String imagen,Long usuarioId) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.km = km;
        this.imagen = imagen;
        this.usuarioId = usuarioId;
        
        
    }
}

