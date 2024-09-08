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

        factura.setUsuario(usuario);

        // Guardar la factura
        factura = facturaRepository.save(factura);

        // Procesar los ítems de la factura
        double montoTotal = 0.0;
        boolean status = true;
        for (FacturasRequest.ItemRequest itemRequest : facturaRequest.getItems()) {
            Producto producto = productoRepository.findById(itemRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            if (itemRequest.getCantidad()> producto.getStock())
                status = false;
        }
        if (status == true){
            for (FacturasRequest.ItemRequest itemRequest : facturaRequest.getItems()) {
                ItemsFactura item = new ItemsFactura();
                item.setCantidad(itemRequest.getCantidad());
            
                Producto producto = productoRepository.findById(itemRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                item.setProducto(producto);
                item.setFactura(factura);
            
                double montoItem = producto.getPrecio() * itemRequest.getCantidad();
                item.setMonto(montoItem);

                montoTotal += montoItem;
                itemsFacturaRepository.save(item);
            }

            // Actualizar el monto total de la factura
            factura.setMonto(montoTotal);
            facturaRepository.save(factura);
        }
        else{
            facturaRepository.delete(factura);
        }
        return factura;
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
                factura.getFecha(),
                factura.getUsuario()
        );
    }
}
