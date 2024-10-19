package com.uade.tpo.MarketPlace.service;

import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.UsuarioRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    

    @Transactional
    public UsuarioRequest obtenerDatos(Usuario usuario){
        return new UsuarioRequest(
            usuario.getNombreUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getMail()
        );
    }
}
