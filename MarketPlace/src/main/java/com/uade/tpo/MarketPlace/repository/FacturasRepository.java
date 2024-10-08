package com.uade.tpo.MarketPlace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.MarketPlace.entity.Facturas;

@Repository
public interface FacturasRepository extends JpaRepository<Facturas,Long>{
    
    @Query("SELECT f FROM Facturas f WHERE f.usuario.id = :usuario_id ORDER BY f.fecha ASC")
    List<Facturas> findAllFacturasUsuario(@Param("usuario_id") Long usuario_id);

    @Query("SELECT f FROM Facturas f WHERE f.id = :id")
    Facturas findFacturaById(@Param("id") Long id);

    @Query("SELECT COUNT(f)=1 FROM Facturas f WHERE f.usuario.id = :usuario_id AND f.id = :id")
    boolean existsFacturaUsuario(@Param("usuario_id") Long usuario_id, @Param("id") Long id);
}
