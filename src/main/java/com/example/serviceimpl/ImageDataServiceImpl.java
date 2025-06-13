package com.example.serviceimpl;

import org.springframework.stereotype.Service;

import com.example.entity.ImageData;
import com.example.repository.ImageDataRepository;
import com.example.service.ImageDataService;

@Service
public class ImageDataServiceImpl implements ImageDataService {

	private final ImageDataRepository imageDataRepository;

    public ImageDataServiceImpl(ImageDataRepository imageDataRepository) {
        this.imageDataRepository = imageDataRepository;
    }
    
	@Override
	public ImageData  getByItemId(Integer itemId) {
		return imageDataRepository.findByItemId(itemId);
	}

}
