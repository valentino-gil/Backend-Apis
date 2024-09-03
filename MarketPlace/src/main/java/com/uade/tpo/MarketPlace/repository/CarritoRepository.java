package com.uade.tpo.MarketPlace.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.MarketPlace.entity.Carrito;


public interface CarritoRepository extends JpaRepository<Carrito,Long>{

    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId")
    List<Carrito> findByUsuarioId(@Param("usuarioId") Long usuarioId);



}
