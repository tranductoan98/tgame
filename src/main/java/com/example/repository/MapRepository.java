package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Maps;
import com.example.enums.MapType;

@Repository
public interface MapRepository extends JpaRepository<Maps, Integer> {
	List<Maps> findByType(MapType type);
}