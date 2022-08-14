package com.InventoryManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.InventoryManagement.entities.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	
	@Query("select o from Orders o")
	public List<Orders> getAllOrders();
	
	@Query(
			value = "select o.id from Orders o order by id desc limit 1",
			nativeQuery = true)
	public int getLatestOrderId();
	
	@Transactional
	@Modifying
	@Query(
			value = "update Orders o set o.price = :price where o.id = :id",
			nativeQuery = true)
	public int setPrice(@Param("price") int price, @Param("id") int id);

	@Query("select o from Orders o where o.userId = :user_id")
	public List<Orders> getOrdersByUserId(@Param("user_id") int user_id);
	
	@Query("select o.id from Orders o where o.userId = :user_id")
	public List<Integer> getOrderIdFromUserId(@Param("user_id") int user_id);
	
	@Query("select o.date from Orders o where o.userId = :user_id")
	public java.sql.Date getDateFromOrderId(@Param("user_id") int user_id);
}
