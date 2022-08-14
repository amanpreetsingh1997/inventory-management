package com.InventoryManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.InventoryManagement.entities.OrderItems;

public interface OrderItemsRepository extends CrudRepository<OrderItems, Integer> {
	
	@Query("select o from OrderItems o where o.order_id = :order_id")
	public List<OrderItems> getOrderItemsFromOrderID(@Param("order_id") int order_id);
	
}
