package com.uade.tpo.MarketPlace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.MarketPlace.service.UsuarioService;
import com.uade.tpo.MarketPlace.entity.dto.UsuarioRequest;

import java.net.URI;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioRequest usuarioRequest) 
        throws UsuarioDuplicateException {
        Usuario result = usuarioService.CrearUsuario(usuarioRequest.getTipoUsuario(), usuarioRequest.getNombreUsuario());
        return ResponseEntity.created(URI.create("/Usuario/" + result.getId())).body(result);
    }
}
