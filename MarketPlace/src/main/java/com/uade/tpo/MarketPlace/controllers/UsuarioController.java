package com.uade.tpo.MarketPlace.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.UsuarioRequest;
import com.uade.tpo.MarketPlace.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
public ResponseEntity<UsuarioRequest> getPerfil(Principal principal) {
    // Usa el principal para obtener el usuario autenticado
    Usuario usuario = usuarioService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Crear un DTO a partir del usuario
    UsuarioRequest usuarioDTO = new UsuarioRequest(usuario);

    return ResponseEntity.ok(usuarioDTO);
}


}
