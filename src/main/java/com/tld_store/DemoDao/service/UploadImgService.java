package com.tld_store.DemoDao.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import exception.CustomException;

@Service
public class UploadImgService {
    public void checkFileUpload(MultipartFile file) {
    	float fileSize = file.getSize();
    	float maxSize = (float) (0.5 * 1024 * 1024); // 2MB
    	if (fileSize > maxSize) { 
    		throw new CustomException("File chỉ được tối đa 500KB.");
    		}
        String contentType = file.getContentType();
        if (!isImage(contentType)) {
            throw new CustomException("Chỉ được upload file có đuôi .jpeg, .png, .gif");
        }
    }

    private boolean isImage(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif");
    }
}
