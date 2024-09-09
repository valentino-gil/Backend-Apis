package com.uade.tpo.MarketPlace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.MarketPlace.entity.Facturas;
import com.uade.tpo.MarketPlace.entity.Role;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest;
import com.uade.tpo.MarketPlace.entity.dto.FacturasRequest.ItemRequest;
import com.uade.tpo.MarketPlace.repository.FacturasRepository;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;
import com.uade.tpo.MarketPlace.service.FacturasService;
import com.uade.tpo.MarketPlace.service.ItemService;




@RestController
@RequestMapping("api/facturas")
public class FacturasController {

    @Autowired
    private FacturasService facturaService;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FacturasRepository facturasRepository;

    @PostMapping
    public ResponseEntity<Facturas> crearFactura(@RequestBody FacturasRequest facturaRequest, 
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = buscarUsuario(userDetails);
        if (usuario.getRole() != Role.Comprador){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Facturas factura = facturaService.crearFactura(facturaRequest,usuario);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FacturasRequest>> obtenerFacturasComprador(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = buscarUsuario(userDetails);
        List<FacturasRequest> facturas = facturaService.obtenerFacturas(usuario);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ItemRequest>> ObtenerItemsFactura(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = buscarUsuario(userDetails);
        boolean status = facturasRepository.existsFacturaUsuario(usuario.getId(), id);
        if (status){
            List<ItemRequest> item = itemService.ObtenerItemsFactura(id, usuario);
            return ResponseEntity.ok(item);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    private Usuario buscarUsuario(UserDetails userDetails){
        String usuario = userDetails.getUsername();
        return UsuarioRepository.findByNombreUsuario(usuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}