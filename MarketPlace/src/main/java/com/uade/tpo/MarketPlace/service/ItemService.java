package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.ItemsFactura;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.FiltroItemRequest;
import com.uade.tpo.MarketPlace.repository.ItemsFacturaRepository;

@Service
public class ItemService {

    @Autowired
    private ItemsFacturaRepository itemsFacturaRepository;


    public List<FiltroItemRequest> ObtenerItemsFactura(Long id, Usuario usuario){
        List<ItemsFactura> items = itemsFacturaRepository.findAllItemsFactura(id);
        return items.stream()
        .map(item -> convertirAFiltroItemRequest(item))
        .collect(Collectors.toList());
    }

    private FiltroItemRequest convertirAFiltroItemRequest(ItemsFactura item) {
        return new FiltroItemRequest(
                item.getId(),
                item.getCantidad(),
                item.getMonto(),
                item.getProducto().getId()
        );
    }
    
}
