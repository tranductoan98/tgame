package com.example.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Items;
import com.example.repository.ItemsRepository;
import com.example.service.ItemsService;

@Service
public class ItemsServiceImpl implements ItemsService{

	private final ItemsRepository itemsRepository;
	
	
	public ItemsServiceImpl(ItemsRepository itemsRepository) {
		super();
		this.itemsRepository = itemsRepository;
	}

	@Override
	public List<Items> getAllItems() {
		return itemsRepository.findAll();
	}

	@Override
	public Optional<Items> getItemById(Integer itemId) {
		return itemsRepository.findById(itemId);
	}

	@Override
	public Items saveItem(Items items) {
		if (items.getName() == null || items.getName().isEmpty()) {
	        throw new IllegalArgumentException("Tên item không được để trống.");
	    }

		return itemsRepository.save(items);
	}

	@Override
	public void deleteItem(Integer itemId) {
		if (!itemsRepository.existsById(itemId)) {
	        throw new IllegalArgumentException("Item không tồn tại với ID = " + itemId);
	    }
		itemsRepository.deleteById(itemId);
	}

}
