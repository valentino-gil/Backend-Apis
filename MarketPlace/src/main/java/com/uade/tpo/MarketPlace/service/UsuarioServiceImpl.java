package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario CrearUsuario(String tipoUsuario, String nombreUsuario) throws UsuarioDuplicateException {
        List<Usuario> usuarios = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuarios.isEmpty()) {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setTipoUsuario(tipoUsuario);
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            return usuarioRepository.save(nuevoUsuario);
        }
        throw new UsuarioDuplicateException();
    }
}
