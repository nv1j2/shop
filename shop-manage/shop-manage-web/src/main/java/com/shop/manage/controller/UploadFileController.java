package com.shop.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.common.service.PropertieService;
import com.shop.common.vo.PicUploadResult;

@Controller
public class UploadFileController {
    
    @Autowired
    private PropertieService propertieService ;
    
    @RequestMapping("/pic/upload")
    @ResponseBody
    public PicUploadResult upload(MultipartFile uploadFile) throws IllegalStateException, IOException {
        
        PicUploadResult result = new PicUploadResult();
        
        String _picFileName = uploadFile.getOriginalFilename();
        String extFileName = _picFileName.substring(_picFileName.lastIndexOf(".")) ;
        
        if (!extFileName.matches("^.*(jpg|png|jpeg|gif|bmp)$")) {
            result.setError(1);
        }else{
            result.setError(0);
        }
        
        BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
        try{
            result.setHeight(bufferedImage.getHeight()+"");
            result.setWidth(bufferedImage.getWidth()+"");
        }catch(Exception e){
            result.setError(1);
        }
        
        //绝对路径.............<---
        String path = propertieService.REPOSITORY_PATH;
        String pathURL = propertieService.IMAGE_BASE_URL;
        String dir =  new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
        String fileName = System.currentTimeMillis() + "" + RandomUtils.nextInt(100, 1000)+extFileName;

        result.setUrl(pathURL + dir + fileName);
        
        File file = new File(path + dir+ fileName) ; 
        
        System.out.println(pathURL + dir + fileName);
        
        if(!file.exists()){
            file.mkdirs();
        }
        
        uploadFile.transferTo(file);
        return result;
        

    }
}
