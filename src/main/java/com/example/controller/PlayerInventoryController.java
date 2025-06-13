package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.InventoryItemDTO;
import com.example.entity.PlayerInventory;
import com.example.service.PlayerInventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/inventory")
public class PlayerInventoryController {
	
	private final PlayerInventoryService inventoryService;

	public PlayerInventoryController(PlayerInventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	@Operation(summary = "Lấy danh sách vật phẩm theo playerId", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/player/{playerId}")
    public ResponseEntity<List<InventoryItemDTO>> getInventoryByPlayerId(@PathVariable Integer playerId, @RequestParam(defaultValue = "false") boolean isEquippedCheck) {
		System.out.print("isEquippedCheck" + isEquippedCheck);
        List<PlayerInventory> items = inventoryService.getAllItemsByPlayerId(playerId, isEquippedCheck);
        List<InventoryItemDTO> itemList = items.stream()
                .map(InventoryItemDTO::new)
                .toList();
        return ResponseEntity.ok(itemList);
    }
	
	@Operation(summary = "Lấy thông tin 1 vật phẩm trong inventory theo ID", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/{id}")
    public ResponseEntity<PlayerInventory> getInventoryById(@PathVariable Integer id) {
        Optional<PlayerInventory> inventory = inventoryService.getInventoryById(id);
        return inventory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	@Operation(summary = "Thêm vật phẩm mới vào inventory", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/addnew")
    public ResponseEntity<PlayerInventory> addItem(@RequestBody PlayerInventory inventory) {
        PlayerInventory saved = inventoryService.addItemToInventory(inventory);
        return ResponseEntity.ok(saved);
    }
	
	@Operation(summary = "Cập nhật thông tin vật phẩm", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/update/{id}")
    public ResponseEntity<PlayerInventory> updateItem(@PathVariable Integer id, @RequestBody PlayerInventory inventory) {
        if (!inventoryService.getInventoryById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        inventory.setId(id);
        PlayerInventory updated = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(updated);
    }
	
	@Operation(summary = "Xóa vật phẩm", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        if (!inventoryService.getInventoryById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
}
