package com.chat.services;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Service
public class MediaService {
    
	private final Cloudinary cloudinary;
	
	    public MediaService(Cloudinary cloudinary) {
	        this.cloudinary = cloudinary;
	    }
    // Add document types to supported formats
    private static final Set<String> SUPPORTED_TYPES = Set.of(
        // Images
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp",
        // Videos  
        "video/mp4", "video/avi", "video/mov", "video/wmv",
        // Audio
        "audio/mp3", "audio/wav", "audio/aac",
        // Documents
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
        "application/vnd.ms-excel", // .xls
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
        "application/msword", // .doc
        "text/plain" // .txt
    );
    
    public String uploadMedia(MultipartFile file) throws IOException {
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !SUPPORTED_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        
        // Validate file size (increase for documents - e.g., max 25MB)
        if (file.getSize() > 25 * 1024 * 1024) {
            throw new IllegalArgumentException("File size too large. Maximum 25MB allowed.");
        }
        
        try {
            // Use "raw" resource type for non-media files
            String resourceType = getResourceType(contentType);
            
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                Map.of("resource_type", resourceType));
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }
    
    private String getResourceType(String contentType) {
        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else if (contentType.startsWith("audio/")) {
            return "video"; 
        } else {
            return "raw"; 
        }
    }
}