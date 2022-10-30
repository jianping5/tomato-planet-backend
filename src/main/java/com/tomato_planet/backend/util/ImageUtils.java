package com.tomato_planet.backend.util;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ImageUtils {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.accessSecretKey}")
    private String accessSecretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.imageUrl}")
    private String url;

    /**
     * 处理多文件
     * @param multipartFiles
     * @return
     */
    public Map<String, List<String>> uploadImages(MultipartFile[] multipartFiles){
        Map<String,List<String>> map = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile file : multipartFiles){
            imageUrls.add(uploadImageQiniu(file));
        }
        map.put("imageUrl",imageUrls);
        return map;
    }

    /**
     * 上传图片到七牛云
     * @param multipartFile
     * @return
     */
    private String uploadImageQiniu(MultipartFile multipartFile){
        try {
            // 获取文件上传的流
            byte[] fileBytes = multipartFile.getBytes();

            // 创建日期目录分隔
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = dateFormat.format(new Date());

            // 获取文件名
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = datePath+"/"+ UUID.randomUUID().toString().replace("-", "")+ suffix;

            // 构造一个带指定 Region 对象的配置类
            // Region.huanan(根据自己的对象空间的地址选择）
            Configuration cfg = new Configuration(Region.huanan());
            UploadManager uploadManager = new UploadManager(cfg);

            // 获取七牛云提供的 token
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            uploadManager.put(fileBytes,filename,upToken);

            return url+filename;
        } catch (IOException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "图片上传异常");
        }
    }
}