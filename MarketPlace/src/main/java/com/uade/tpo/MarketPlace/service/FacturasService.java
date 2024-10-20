package com.uade.tpo.MarketPlace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Carrito;
import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.ItemsFactura;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest;
import com.uade.tpo.MarketPlace.repository.CarritoRepository;
import com.uade.tpo.MarketPlace.repository.FacturasRepository;
import com.uade.tpo.MarketPlace.repository.ItemsFacturaRepository;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class FacturasService {

    @Autowired
    private FacturasRepository facturaRepository;

    @Autowired
    private ItemsFacturaRepository itemsFacturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    
    private List<String> descuentos = new ArrayList<>();

    @Transactional
    public FacturasRequest crearFactura(Usuario usuario, String descuento) {
        // Crear la nueva factura
        Facturas factura = new Facturas();
        factura.setFecha(new java.sql.Date(System.currentTimeMillis()));
        factura.setMonto(0.0); // El monto se calculará más tarde
        factura.setDescuento(0.0);
        factura.setUsuario(usuario);
        
        // Guardar la factura primero
        Facturas facturaGuardada = facturaRepository.save(factura);

        // Procesar los ítems de la factura
        double montoTotal = 0.0;

        List<Carrito> carrito = carritoRepository.findByUsuarioId(usuario.getId());
        
        // Validar stock antes de procesar la factura
        for (Carrito c : carrito) {
            Producto producto = productoRepository.findById(c.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < c.getCantidad()) {
                facturaRepository.delete(factura);
                // Si no hay suficiente stock, lanzamos una excepción
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getModelo());
            }
        }

        // Crear los ítems de la factura y calcular el monto total
        for (Carrito c : carrito) {
            ItemsFactura item = new ItemsFactura();
            item.setCantidad(c.getCantidad());

            Producto producto = productoRepository.findById(c.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Reducir el stock del producto
            producto.setStock(producto.getStock() - c.getCantidad());
            productoRepository.save(producto); // Guardar la actualización de stock

            item.setProducto(producto);
            item.setFactura(facturaGuardada);

            double montoItem = producto.getPrecio() * c.getCantidad();
            item.setMonto(montoItem);

            montoTotal += montoItem;
            itemsFacturaRepository.save(item); // Guardar el ítem de la factura
        }

        // Vaciar el carrito del usuario
        carritoRepository.deleteByUsuarioId(usuario.getId());

        // Inicializar descuentos
        inicializarDescuentos();
        
        // Aplicar descuentos
        if (descuentos.contains(descuento)) {
            montoTotal *= 0.90; // Descuento del 10%
            facturaGuardada.setDescuento(0.10);
        }

        // Verificar si el monto total es cero y eliminar la factura si no se procesaron ítems válidos
        if (montoTotal == 0.0) {
            facturaRepository.delete(facturaGuardada);
            throw new RuntimeException("No se pudo procesar la factura debido a falta de stock en todos los productos.");
        }

        // Actualizar el monto total de la factura
        facturaGuardada.setMonto(montoTotal);
        facturaRepository.save(facturaGuardada);

        // Convertir la factura guardada a un DTO para devolverla
        return convertirAFacturaRequest(facturaGuardada);
    }

    public List<FacturasRequest> obtenerFacturas(Usuario usuario){
        List<Facturas> facturas = facturaRepository.findAllFacturasUsuario(usuario.getId());
        return facturas.stream()
        .map(factura -> convertirAFacturaRequest(factura))
        .collect(Collectors.toList());
    }

    private FacturasRequest convertirAFacturaRequest(Facturas factura) {
        return new FacturasRequest(
                factura.getId(),
                factura.getMonto(),
                factura.getDescuento(),
                factura.getFecha(),
                factura.getUsuario().getId()
        );
    }

    // Método que inicializa la lista de descuentos
    private void inicializarDescuentos(){
        descuentos.add("bienvenida");
        descuentos.add("descuento10");
    }
}

