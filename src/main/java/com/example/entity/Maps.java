package com.example.entity;

import com.example.enums.MapType;

import jakarta.persistence.*;

@Entity
@Table(name = "maps")
public class Maps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    private String name;

    // Số lượng tiles theo chiều ngang và dọc
    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    // Kích thước 1 tile (tính theo pixel)
    @Column(name = "tile_width", nullable = false)
    private Integer tileWidth = 32;

    @Column(name = "tile_height", nullable = false)
    private Integer tileHeight = 32;

    // Kiểu map (town, house, dungeon...)
    @Column(nullable = false, length = 32)
    private MapType type;

    @Column(length = 255)
    private String description;

    // Có phải là bản đồ mặc định khi login không
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    // --- GETTER / SETTER ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(Integer tileWidth) {
        this.tileWidth = tileWidth;
    }

    public Integer getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(Integer tileHeight) {
        this.tileHeight = tileHeight;
    }

    public MapType getType() {
		return type;
	}

	public void setType(MapType type) {
		this.type = type;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}

