package com.uade.tpo.MarketPlace.service;



import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.exceptions.UsuarioDuplicateException;


public interface UsuarioService {
  public Usuario CrearUsuario(String TipoUsuario,String NombreUsuario) throws UsuarioDuplicateException;
}
