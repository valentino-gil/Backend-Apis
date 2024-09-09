package com.uade.tpo.MarketPlace.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.ItemsFactura;
import com.uade.tpo.MarketPlace.entity.Producto;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest;
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
        factura.setFecha((java.sql.Date) new Date());
        factura.setMonto(0.0); // El monto se calculará más tarde
        factura.setDescuento(0.0);

        factura.setUsuario(usuario);


        // Procesar los ítems de la factura
        double montoTotal = 0.0;
        for (FacturasRequest.ItemRequest itemRequest : facturaRequest.getItems()) {
            Producto producto = productoRepository.findById(itemRequest.getProductoId().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            if (itemRequest.getCantidad()> producto.getStock())
                new RuntimeException("Stock "+producto.getModelo()+" insuficiente");
        }

            for (FacturasRequest.ItemRequest itemRequest : facturaRequest.getItems()) {
                ItemsFactura item = new ItemsFactura();
                item.setCantidad(itemRequest.getCantidad());
            
                Producto producto = productoRepository.findById(itemRequest.getProductoId().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                item.setProducto(producto);
                item.setFactura(factura);
            
                double montoItem = producto.getPrecio() * itemRequest.getCantidad();
                item.setMonto(montoItem);

                montoTotal += montoItem;
                itemsFacturaRepository.save(item);
            }

            if(montoTotal>=500){
                montoTotal=montoTotal*0.95;
                factura.setDescuento(0.05);
            }
            else if(montoTotal>=1000){
                montoTotal=montoTotal*0.90;
                factura.setDescuento(0.10);
            }

            // Actualizar el monto total de la factura
            factura.setMonto(montoTotal);
        
            
        return  facturaRepository.save(factura);

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
                factura.getUsuario()
        );
    }
}
