package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer> {
	 List<Items> findByType(int type);
}
