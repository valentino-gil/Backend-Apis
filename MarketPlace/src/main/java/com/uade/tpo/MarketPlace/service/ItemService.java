package com.uade.tpo.MarketPlace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.MarketPlace.entity.ItemsFactura;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.entity.dto.ItemRequest;
import com.uade.tpo.MarketPlace.repository.ItemsFacturaRepository;

@Service
public class ItemService {

    @Autowired
    private ItemsFacturaRepository itemsFacturaRepository;


    public List<ItemRequest> ObtenerItemsFactura(Long id, Usuario usuario){
        List<ItemsFactura> items = itemsFacturaRepository.findAllItemsFactura(id);
        return items.stream()
        .map(item -> convertirAItemRequest(item))
        .collect(Collectors.toList());
    }

    private ItemRequest convertirAItemRequest(ItemsFactura item) {
        return new ItemRequest(
                item.getProducto().getId(),
                item.getCantidad(),
                item.getMonto(),
                item.getId(),
                item.getFactura()
        );
    }
    
}
