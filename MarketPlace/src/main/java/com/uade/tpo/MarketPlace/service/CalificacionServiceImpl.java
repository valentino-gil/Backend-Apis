package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Calificacion;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.CalificacionRequest;
import com.uade.tpo.MarketPlace.repository.CalificacionRepository;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalificacionServiceImpl implements CalificacionService{
    private final CalificacionRepository calificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;  // Para la lógica futura

    public CalificacionRequest agregarCalificacion(Long vendedorId, String emailComprador, int puntaje, String comentario) {
        Usuario vendedor = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        Usuario comprador = usuarioRepository.findBymail(emailComprador)
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));

        Calificacion calificacion = new Calificacion();
        calificacion.setPuntaje(puntaje);
        calificacion.setComentario(comentario);
        calificacion.setVendedor(vendedor);
        calificacion.setComprador(comprador);

        calificacionRepository.save(calificacion);

        return new CalificacionRequest(calificacion.getId(), calificacion.getPuntaje(),
                                       calificacion.getComentario(),
                                       calificacion.getComprador().getId(),
                                       calificacion.getVendedor().getId());
    }

    public List<CalificacionRequest> obtenerCalificacionesDeVendedor(Long vendedorId) {
        List<Calificacion> calificaciones = calificacionRepository.findByVendedorId(vendedorId);

        return calificaciones.stream()
                .map(c -> new CalificacionRequest(c.getId(), c.getPuntaje(),
                                                  c.getComentario(),
                                                  c.getComprador().getId(),
                                                  c.getVendedor().getId()))
                .collect(Collectors.toList());
    }
}
