package com.uade.tpo.MarketPlace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.MarketPlace.entity.Calificacion;
import com.uade.tpo.MarketPlace.entity.Usuario;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByVendedorId(Long vendedorId);
    
    // Lógica futura: encontrar si un comprador ya ha calificado al vendedor.
    boolean existsByCompradorAndVendedor(Usuario comprador, Usuario vendedor);
}
