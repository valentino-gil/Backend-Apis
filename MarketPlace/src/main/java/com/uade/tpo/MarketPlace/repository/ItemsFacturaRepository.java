package com.uade.tpo.MarketPlace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.MarketPlace.entity.ItemsFactura;

public interface ItemsFacturaRepository extends JpaRepository<ItemsFactura,Long>{
    
    @Query("SELECT items FROM ItemsFactura items WHERE items.factura.id = :factura_id")
    List<ItemsFactura> findAllItemsFactura(@Param("factura_id") Long factura_id);

    @Query("SELECT item FROM ItemsFactura item WHERE item.id = :item_id")
    ItemsFactura findItemFactura(@Param("item_id") Long id);
}
