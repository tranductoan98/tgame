package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class AvatarService {

    private final String AVATAR_UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/avatars/";

    public String saveAvatar(MultipartFile file, Integer userId, Integer playerId, String oldAvatarPath) throws IOException {
		File dir = new File(AVATAR_UPLOAD_DIR);
		if (!dir.exists()) {
		    dir.mkdirs();
		}
		
		deleteOldAvatar(oldAvatarPath);
		String originalName = file.getOriginalFilename();
		String extension = getFileExtension(originalName);
		String baseName = getBaseName(originalName);
        String safeBaseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String filename = String.format("%s_u%d_p%d%s", safeBaseName, userId, playerId, extension);

        Path filePath = Paths.get(AVATAR_UPLOAD_DIR, filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        return "/avatars/" + filename;
    }
    
    private void deleteOldAvatar(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            String filename = Paths.get(avatarPath).getFileName().toString();
            Path path = Paths.get(AVATAR_UPLOAD_DIR, filename);
            File file = path.toFile();
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("Không thể xóa avatar cũ: " + file.getAbsolutePath());
                } else {
                    System.out.println("Xóa avatar cũ thành công: " + file.getAbsolutePath());
                }
            } else {
                System.err.println("File không tồn tại hoặc không hợp lệ: " + file.getAbsolutePath());
            }
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.lastIndexOf(".") != -1) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return ".png";
    }
    
    private String getBaseName(String filename) {
        if (filename == null) return "avatar";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;
    }
}
