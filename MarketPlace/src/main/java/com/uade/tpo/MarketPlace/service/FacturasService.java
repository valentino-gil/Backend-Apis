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

        // Crear los ítems de la factura y calcular el monto total
        for (Carrito carry : carrito) {
            ItemsFactura item = new ItemsFactura();
            item.setCantidad(carry.getCantidad());

            Producto p = productoRepository.findById(carry.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Reducir el stock del producto
            p.setStock(p.getStock() - carry.getCantidad());
            productoRepository.save(p); // Guardar la actualización de stock

            item.setProducto(p);
            item.setFactura(facturaGuardada);

            double montoItem = p.getPrecio() * carry.getCantidad();
            item.setMonto(montoItem);

            montoTotal += montoItem;
            itemsFacturaRepository.save(item); // Guardar el ítem de la factura
        }

    //Vaciar el carrito del usuario
    List<Carrito> carritos = carritoRepository.findByUsuarioId(usuario.getId());
    for (Carrito cart : carritos) {
        carritoRepository.delete(cart);
    }

    inicializarDescuentos();
    // Aplicar descuentos
    if (descuentos.contains(descuento)) {
        montoTotal *= 0.90; // Descuento del 10%
        facturaGuardada.setDescuento(0.10);
    }else{
        facturaGuardada.setDescuento(0.0);
    }

        // Verificar si el monto total es cero y eliminar la factura si no se procesaron ítems válidos
        if (montoTotal == 0.0) {
            facturaRepository.delete(facturaGuardada);
            throw new RuntimeException("No se pudo procesar la factura debido a falta de stock en todos los productos.");
        }

        // Actualizar el monto total de la factura
        facturaGuardada.setMonto(montoTotal);
        facturaRepository.save(facturaGuardada);
    }
        // Convertir la factura guardada a un DTO para devolverla
        return convertirAFacturaRequest(facturaGuardada);
    

}

    public List<FacturasRequest> obtenerFacturas(Usuario usuario){
        List<Facturas> facturas = facturaRepository.findAllFacturasUsuario(usuario.getId());
        return facturas.stream()
        .map(factura -> convertirAFacturaRequest(factura))
        .collect(Collectors.toList());
    }

    public double obtenerDescuento(String descuento){
        inicializarDescuentos();
        if (descuentos.contains(descuento))
            return 0.10;
        return 0;
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
        descuentos = new ArrayList<>();
        descuentos.add("bienvenida");
        descuentos.add("descuento10");
    }




    public FacturasRequest comprarAhora(Usuario usuario, String descuento, long producto) {

        Producto p = productoRepository.findById(producto)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Facturas factura = new Facturas();
    factura.setFecha(new java.sql.Date(System.currentTimeMillis()));
    factura.setMonto(p.getPrecio()); // El monto se calculará más tarde
    inicializarDescuentos();
    if(descuentos.contains(descuento)){
        factura.setDescuento(0.1);
    }
    else{
        factura.setDescuento(0);
    }
    factura.setUsuario(usuario);
    // Guardar la factura primero
    Facturas facturaGuardada = facturaRepository.save(factura);

    ItemsFactura item = new ItemsFactura();
    item.setCantidad(1);
    item.setMonto(p.getPrecio());


    p.setStock(p.getStock() - 1);
            productoRepository.save(p); // Guardar la actualización de stock

            item.setProducto(p);
            item.setFactura(facturaGuardada);

            itemsFacturaRepository.save(item);
            return convertirAFacturaRequest(facturaGuardada);
        }

    }







