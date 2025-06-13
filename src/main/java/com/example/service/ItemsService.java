package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entity.Items;

public interface ItemsService {
	List<Items> getAllItems();
	public Optional<Items> getItemById(Integer itemId);
	public Items saveItem(Items items);
	public void deleteItem(Integer itemId);
}
