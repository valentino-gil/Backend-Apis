package com.uade.tpo.MarketPlace.controllers;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.UsuarioRequest;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public ResponseEntity<UsuarioRequest> obtenerDatos(@AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseEntity.ok(usuarioService.obtenerDatos(buscarUsuario(userDetails)));
    }
    
    private Usuario buscarUsuario(UserDetails userDetails){
        String usuario = userDetails.getUsername();
        return usuarioRepository.findByMail(usuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
