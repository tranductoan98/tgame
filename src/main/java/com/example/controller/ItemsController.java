package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Items;
import com.example.service.ItemsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/items")
public class ItemsController {
	
	private final ItemsService itemsService;
	
	@Autowired
	public ItemsController(ItemsService itemsService) {
		this.itemsService = itemsService;
	}
	
	@Operation(summary = "Lấy toàn bộ item", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/all")
    public List<Items> getAll() {
        return itemsService.getAllItems();
    }
	
	@Operation(summary = "Lấy item theo id", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/{itemId}")
    public ResponseEntity<Items> getById(@PathVariable Integer itemId) {
        return itemsService.getItemById(itemId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
	
	@Operation(summary = "tạo 1 item mới", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/create")
    public ResponseEntity<Items> create(@RequestBody Items item) {
		System.out.println("name: " + item.getName());
		Items items = itemsService.saveItem(item);
		return ResponseEntity.status(HttpStatus.CREATED).body(items);
    }
	
	@Operation(summary = "chỉnh sửa item", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/{id}")
    public ResponseEntity<Items> update(@PathVariable Integer itemId, @RequestBody Items item) {
        return itemsService.getItemById(itemId).map(existing -> {
            item.setItemId(itemId);
            return ResponseEntity.ok(itemsService.saveItem(item));
        }).orElse(ResponseEntity.notFound().build());
    }

	@Operation(summary = "chỉnh sửa item", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
		itemsService.deleteItem(id);
    }
	
}
