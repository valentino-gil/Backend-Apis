package com.uade.tpo.MarketPlace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.UsuarioRequest;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByMail(username);
    }

    public UsuarioRequest getUserProfile() {
        // Obtiene el usuario autenticado desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<Usuario> optionalUsuario = findByUsername(username);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                // Devuelve un DTO con la información necesaria
                return new UsuarioRequest(usuario);
            }
        }
        return null; // O puedes lanzar una excepción si el usuario no está autenticado
    }
}
