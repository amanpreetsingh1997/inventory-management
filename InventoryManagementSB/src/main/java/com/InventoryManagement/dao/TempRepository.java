package com.InventoryManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.InventoryManagement.entities.Temp;

public interface TempRepository extends JpaRepository<Temp, Integer> {
	
	@Query("select t from Temp t")
	public Temp getInfo();
}
