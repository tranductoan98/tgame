package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.MapImageData;
import com.example.entity.Maps;

@Service
public class MapCollisionService {

    private final Map<Integer, boolean[][]> mapWalkableGrids = new ConcurrentHashMap<>();

    private final MapImageDataService mapImageDataService;
    private final MapService mapService;

    @Autowired
    public MapCollisionService(MapImageDataService mapImageDataService, MapService mapService) {
        this.mapImageDataService = mapImageDataService;
        this.mapService = mapService;
    }

    public void loadFromDatabase(int mapId) throws Exception {
        Optional<Maps> mapOpt = mapService.getMapById(mapId);
        if (mapOpt.isEmpty()) throw new RuntimeException("Không tìm thấy mapId: " + mapId);

        Maps map = mapOpt.get();
        int width = map.getWidth();
        int height = map.getHeight();

        boolean[][] grid = new boolean[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                grid[x][y] = false;

        List<MapImageData> tiles = mapImageDataService.getByMapId(mapId);
        for (MapImageData tile : tiles) {
            int x = tile.getRenderX();
            int y = tile.getRenderY();
            if (x >= 0 && x < width && y >= 0 && y < height) {
                grid[x][y] = tile.getIsWalkable();
            }
        }

        mapWalkableGrids.put(mapId, grid);
    }

    public boolean isMapLoaded(int mapId) {
        return mapWalkableGrids.containsKey(mapId);
    }

    public boolean isPositionValid(int mapId, float x, float y) {
        boolean[][] grid = mapWalkableGrids.get(mapId);
        if (grid == null) return false;

        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);

        if (ix < 0 || iy < 0 || ix >= grid.length || iy >= grid[0].length) return false;

        return grid[ix][iy];
    }
}