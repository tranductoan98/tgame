//package com.example.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.entity.Maps;
//
//@Service
//public class MapFileService {
//
//	private final String MAP_UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/maps/";
//	
//	public String saveMapFile(MultipartFile file, Maps map) throws IOException {
//        File dir = new File(MAP_UPLOAD_DIR);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        
//        deleteOldMap(map.getMapFile());
//        
//        String originalName = file.getOriginalFilename();
//        String extension = getFileExtension(originalName);
//        String baseName = getBaseName(map.getName());
//        String safeBaseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "_");
//        String filename = String.format("%s_%d%s", safeBaseName, System.currentTimeMillis(), extension);
//
//        Path filePath = Paths.get(MAP_UPLOAD_DIR, filename);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        return "/maps/" + filename;
//    }
//
//    private String getFileExtension(String filename) {
//        if (filename != null && filename.lastIndexOf(".") != -1) {
//            return filename.substring(filename.lastIndexOf("."));
//        }
//        return ".tmx";
//    }
//
//    private String getBaseName(String name) {
//        if (name == null) return "map";
//        int dotIndex = name.lastIndexOf('.');
//        return (dotIndex > 0) ? name.substring(0, dotIndex) : name;
//    }
//    
//    private void deleteOldMap(String mapPath) {
//        if (mapPath != null && !mapPath.isEmpty()) {
//            String filename = Paths.get(mapPath).getFileName().toString();
//            Path path = Paths.get(MAP_UPLOAD_DIR, filename);
//            File file = path.toFile();
//            if (file.exists() && file.isFile()) {
//                boolean deleted = file.delete();
//                if (!deleted) {
//                    System.err.println("Không thể xóa map cũ: " + file.getAbsolutePath());
//                } else {
//                    System.out.println("Xóa map cũ thành công: " + file.getAbsolutePath());
//                }
//            } else {
//                System.err.println("File không tồn tại hoặc không hợp lệ: " + file.getAbsolutePath());
//            }
//        }
//    }
//}
