package com.InventoryManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.InventoryManagement.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
	
	@Query("select b from Brand b")
	public List<Brand> getBrands();
	
	@Query("select b.id from Brand b where b.name= :name")
	public int getBrandId(@Param("name") String brand_name);
	
}
