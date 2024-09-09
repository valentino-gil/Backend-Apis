package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.ItemsFactura;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest;
import com.uade.tpo.MarketPlace.entity.dto.ItemRequest;
import com.uade.tpo.MarketPlace.repository.FacturasRepository;
import com.uade.tpo.MarketPlace.repository.ItemsFacturaRepository;
import com.uade.tpo.MarketPlace.repository.ProductoRepository;

@Service
public class FacturasService {

    @Autowired
    private FacturasRepository facturaRepository;

    @Autowired
    private ItemsFacturaRepository itemsFacturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Facturas crearFactura(FacturasRequest facturaRequest, Usuario usuario) {
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

    
        // Crear los ítems de la factura y calcular el monto total
        for (ItemRequest itemRequest : facturaRequest.getItems()) {
            ItemsFactura item = new ItemsFactura();
            item.setCantidad(itemRequest.getCantidad());

            Producto producto = productoRepository.findById(itemRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < itemRequest.getCantidad())
                facturaRepository.delete(facturaGuardada);
            else{
                producto.setStock(producto.getStock()-itemRequest.getCantidad());
                productoRepository.save(producto);
            }
            item.setProducto(producto);
            item.setFactura(facturaGuardada);
    
            double montoItem = producto.getPrecio() * itemRequest.getCantidad();
            item.setMonto(montoItem);
    
            montoTotal += montoItem;
            itemsFacturaRepository.save(item);
        }
    
        // Aplicar descuentos
        if (montoTotal >= 1000) {
            montoTotal *= 0.90; // Descuento del 10%
            facturaGuardada.setDescuento(0.10);
        } else if (montoTotal >= 500) {
            montoTotal *= 0.95; // Descuento del 5%
            facturaGuardada.setDescuento(0.05);
        }
    
        // Actualizar el monto total de la factura
        facturaGuardada.setMonto(montoTotal);
    
        // Guardar la factura actualizada y devolverla
        return facturaRepository.save(facturaGuardada);
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
}
