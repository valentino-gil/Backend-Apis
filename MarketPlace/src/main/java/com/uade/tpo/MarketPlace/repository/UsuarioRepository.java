package com.uade.tpo.MarketPlace.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.MarketPlace.entity.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

<<<<<<< Updated upstream
    @Query(value = "select u from Usuario u where u.NombreUsuario = ?2")
    static
    List<Usuario> findByNombreUsuario(String NombreUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombreUsuario'");
    }
=======
    // Consulta para buscar usuarios por nombre de usuario
    @Query("SELECT u FROM Usuario u WHERE u.NombreUsuario = :nombreUsuario")
    List<Usuario> findByNombreUsuario(String nombreUsuario);

    // Consulta para buscar usuarios por mail
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail")
    Optional<Usuario> findBymail(String mail);

    // Consulta para verificar si un usuario existe por ID
    @Query("SELECT u FROM Usuario u WHERE u.id = :idUsuario")
    List<Usuario> findByIdUsuario(Long idUsuario);
    Optional<Usuario> findByMail(String mail);
>>>>>>> Stashed changes
}
