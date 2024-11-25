package com.uade.tpo.MarketPlace.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Calificacion;
import com.uade.tpo.MarketPlace.entity.dto.CalificacionRequest;
import com.uade.tpo.MarketPlace.service.CalificacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    @Autowired
    private CalificacionService calificacionService;

    //metodo para calificar al vendedor
    @PostMapping("/{vendedorId}")
public ResponseEntity<?> agregarCalificacion(@PathVariable Long vendedorId,
                                             @RequestBody CalificacionRequest calificacionRequest,
                                             Principal principal) {
    String emailComprador = principal.getName();
    CalificacionRequest calificacion = calificacionService.agregarCalificacion(
        vendedorId,
        emailComprador,
        calificacionRequest.getPuntaje(),
        calificacionRequest.getComentario()
    );
    return ResponseEntity.ok(calificacion);
}


    @GetMapping("/{vendedorId}")
    public ResponseEntity<List<CalificacionRequest>> obtenerCalificaciones(@PathVariable Long vendedorId) {
        List<CalificacionRequest> calificaciones = calificacionService.obtenerCalificacionesDeVendedor(vendedorId);
        return ResponseEntity.ok(calificaciones);
    }
}
