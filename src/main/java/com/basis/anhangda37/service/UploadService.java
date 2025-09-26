package com.basis.anhangda37.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext){
        this.servletContext = servletContext;
    }

    
    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        if(file == null || file.isEmpty() || file.getName().equals("")) {
            return "";
        }
        String finalSavedPath = "";
        try {
            byte[] bytes;
            bytes = file.getBytes();
            String rootPath = this.servletContext.getRealPath("/resources/images");
            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            finalSavedPath += "/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

            File serverFile = new File(dir.getAbsolutePath() + finalSavedPath);

            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finalSavedPath = "/images/" + targetFolder + finalSavedPath;
        return finalSavedPath;
    }

}
