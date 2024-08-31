package com.uade.tpo.MarketPlace.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.MarketPlace.entity.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Consulta para buscar usuarios por nombre de usuario
    @Query("SELECT u FROM Usuario u WHERE u.NombreUsuario = :nombreUsuario")
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Consulta para verificar si un usuario existe por ID
    @Query("SELECT u FROM Usuario u WHERE u.id = :idUsuario")
    List<Usuario> findByIdUsuario(Long idUsuario);
    Optional<Usuario> findByMail(String mail);
    // Consulta para buscar usuarios por mail
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail")
    Optional<Usuario> findBymail(String mail);
}
