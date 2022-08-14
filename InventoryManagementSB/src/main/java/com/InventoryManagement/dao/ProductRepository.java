package com.InventoryManagement.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.InventoryManagement.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("select p.product_id from Product p where p.product_name = :product_name")
	public int getProductId(@Param("product_name") String product_name);
	
	@Query("select p from Product p where p.product_id = :product_id")
	public Product getProductFromId(@Param("product_id") int product_id);
	
	@Query("select p.quantity from Product p where product_id = :product_id")
	public int getProductQuantity(@Param("product_id") int product_id);
	
	@Query("select p.price from Product p where product_id = :product_id")
	public int getProductPrice(@Param("product_id") int product_id);
	
	@Query("select p.product_name from Product p where product_id = :product_id")
	public String getProductName(@Param("product_id") int product_id);
	
	@Transactional
	@Modifying
	@Query(
		value = "update Product p set p.brand_id = :brand_id where p.product_id = :product_id",
		nativeQuery = true)
	public void updateBrandIdAgainstProductId(@Param("product_id") int product_id, @Param("brand_id") int brand_id);
	
	@Query("select p from Product p")
	public List<Product> getProducts();
	
	@Transactional
	@Modifying
	@Query("update Product p set p.quantity = :quantity, p.price = :price where product_id = :product_id")
	public void updateProductQuantity(@Param("product_id") int product_id, @Param("quantity") int quantity, @Param("price") int price);
	
	@Query(
		value = "select * from Product p where p.brand_id = :brand_id",
		nativeQuery = true)
	public List<Product> getProductInfoByBrandId(@Param("brand_id") int brand_id);
	
	@Transactional
	@Modifying
	@Query(
			value = "delete from Product p where p.product_id = :product_id AND p.brand_id = :brand_id",
			nativeQuery = true)
	public void deleteProduct(@Param("product_id") int product_id, @Param("brand_id") int brand_id);

}
