package com.example.entity;

import com.example.enums.MapType;

import jakarta.persistence.*;

@Entity
@Table(name = "maps")
public class Maps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mapid;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "mapfile", length = 255, nullable = false)
    private String mapFile;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MapType type;

    @Column(name = "spawnx", nullable = false)
    private int spawnX;

    @Column(name = "spawny", nullable = false)
    private int spawnY;

	public Integer getMapid() {
		return mapid;
	}

	public void setMapid(Integer mapid) {
		this.mapid = mapid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapFile() {
		return mapFile;
	}

	public void setMapFile(String mapFile) {
		this.mapFile = mapFile;
	}

	public MapType getType() {
		return type;
	}

	public void setType(MapType type) {
		this.type = type;
	}

	public int getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
    
    
}
