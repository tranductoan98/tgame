package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ImageData;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Integer> {
	ImageData findByItemId(Integer itemId);
}

