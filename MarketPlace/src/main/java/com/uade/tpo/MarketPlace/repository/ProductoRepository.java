package com.uade.tpo.MarketPlace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.MarketPlace.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("select count(p) > 0 from Producto p where p.modelo = :modelo and p.marca = :marca and p.usuario.id = :usuarioId")
    boolean existsByMarcaAndModeloAndUsuarioId(@Param("marca") String marca, @Param("modelo") String modelo, @Param("usuarioId") Long usuarioId);
}
