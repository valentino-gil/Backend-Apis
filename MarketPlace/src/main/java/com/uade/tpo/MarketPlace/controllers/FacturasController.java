package com.uade.tpo.MarketPlace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.Role;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.FacturasService;


@RestController
@RequestMapping("api/facturas")
public class FacturasController {

    @Autowired
    private FacturasService facturaService;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @PostMapping
    public ResponseEntity<Facturas> crearFactura(@RequestBody FacturasRequest facturaRequest, 
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        String usuarioActual = userDetails.getUsername();
        Usuario usuario = UsuarioRepository.findByNombreUsuario(usuarioActual)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Facturas factura = facturaService.crearFactura(facturaRequest,usuario);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FacturasRequest>> obtenerFacturasComprador(@AuthenticationPrincipal UserDetails userDetails) {
        String usuarioActual = userDetails.getUsername();
        Usuario usuario = UsuarioRepository.findByNombreUsuario(usuarioActual)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<FacturasRequest> facturas = facturaService.obtenerFacturas(usuario);
        return ResponseEntity.ok(facturas);
    }
    
}