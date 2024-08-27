package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.Locale.Category;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

public class UsuarioServiceImpl implements UsuarioService{

    
    public Usuario CrearUsuario(String TipoUsuario,String NombreUsuario) throws UsuarioDuplicateException {
        List<Usuario> Usuarios = UsuarioRepository.findByNombreUsuario(NombreUsuario);
        if (Usuarios.isEmpty())
            return UsuarioRepository.save(new Usuarios(TipoUsuario,NombreUsuario));
        throw new UsuarioDuplicateException();
    }
    
}
