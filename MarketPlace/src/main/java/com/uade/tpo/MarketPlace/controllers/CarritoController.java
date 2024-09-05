package com.uade.tpo.MarketPlace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Carrito;
import com.uade.tpo.MarketPlace.entity.dto.ProductoRequest;
import com.uade.tpo.MarketPlace.service.CarritoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


    

    @RestController
    @RequestMapping("carrito")

    public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    
    @GetMapping
    public ResponseEntity<List<Carrito>> obtenerCarrito() {
        List<Carrito> carritos = carritoService.obtenerCarrito();
        return ResponseEntity.ok(carritos);
    }


}
