package com.uade.tpo.MarketPlace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.demo.entity.dto.UsuarioRequest;

import java.net.URI;
import java.util.Locale.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("Usuario")
public class UsuarioController {
    




    @PostMapping
    public ResponseEntity<Usuario> CrearUsuario(@RequestBody UsuarioRequest UsuarioRequest) 
        throws UsuarioDuplicateException {
        Usuario result = UsuarioService.CrearUsuario(UsuarioRequest.getNombreUsuario(),UsuarioRequest.getTipoUsuario());//falta crear usuario service
        return ResponseEntity.created(URI.create("/Usuario/" + result.getId())).body(result);
    }
    
 
}
