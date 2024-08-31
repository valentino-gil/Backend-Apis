package com.uade.tpo.MarketPlace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.MarketPlace.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("select count(p) > 0 from Producto p where p.modelo = :modelo and p.marca = :marca and p.usuario.id = :usuarioId and p.año = :año")
    boolean existsByMarcaAndModeloAndUsuarioId(@Param("marca") String marca, @Param("modelo") String modelo, @Param("usuarioId") Long usuarioId, @Param("año") int año);
    @Query("SELECT p FROM Producto p WHERE "
       + "(:marca IS NULL OR p.marca = :marca) AND "
       + "(:modelo IS NULL OR p.modelo = :modelo) AND "
       + "(:precioMin IS NULL OR p.precio >= :precioMin) AND "
       + "(:precioMax IS NULL OR p.precio <= :precioMax) AND "
       + "(:añoMin IS NULL OR p.año >= :añoMin) AND "
       + "(:añoMax IS NULL OR p.año <= :añoMax)")
List<Producto> filtrarProductos(
    @Param("marca") String marca,
    @Param("modelo") String modelo,
    @Param("precioMin") Double precioMin,
    @Param("precioMax") Double precioMax,
    @Param("añoMin") Integer añoMin,
    @Param("añoMax") Integer añoMax
    
);
}
