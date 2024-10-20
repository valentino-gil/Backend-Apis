package com.uade.tpo.MarketPlace.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.MarketPlace.entity.Carrito;


public interface CarritoRepository extends JpaRepository<Carrito,Long>{

    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId")
    List<Carrito> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Carrito c WHERE c.producto.id = :producto AND c.usuario.id = :usuario")
    Optional<Carrito> findByProducto(@Param("producto") Long producto, @Param("usuario") Long usuario);

    @Query("DELETE FROM Carrito c WHERE c.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

}
