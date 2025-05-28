package com.example.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.handler.TmxMapLoader;

@Service
public class TmxMapService {

	private final Map<Integer, boolean[][]> mapWalkableGrids = new ConcurrentHashMap<Integer, boolean[][]>();
    private final TmxMapLoader tmxMapLoader = new TmxMapLoader();

//    @PostConstruct
//    public void init() {
//        try {
//            loadMap(1, "src/main/resources/maps/map1.tmx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public void loadMap(int mapId, String tmxFilePath) throws Exception {
        boolean[][] walkableGrid = tmxMapLoader.loadCollisionLayer(tmxFilePath);
        mapWalkableGrids.put(mapId, walkableGrid);
    }

    public boolean isPositionValid(int mapId, int x, int y) {
        boolean[][] grid = mapWalkableGrids.get(mapId);
        if (grid == null) return false;
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) return false;
        return grid[x][y];
    }
    
}
