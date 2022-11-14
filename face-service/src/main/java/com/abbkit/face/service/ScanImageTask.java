package com.abbkit.face.service;

import cn.hutool.crypto.SecureUtil;
import com.abbkit.face.engine.FaceEngine;
import com.abbkit.face.engine.model.FaceFeature;
import com.abbkit.face.service.entity.FaceRecordEntity;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ScanImageTask {

    @Autowired
    private FaceEngine faceEngine;

    @Autowired
    private FaceService faceService;

    private DefaultIdentifierGenerator identifierGenerator=new DefaultIdentifierGenerator();

    @Scheduled(cron="0/5 * * * * ?")
    public void scan() throws Exception {

        String fileDirPath = "D:\\face\\images";
        log.info("scan file dir:"+fileDirPath);
        File dir=new File(fileDirPath);
        File[] listFiles = dir.listFiles();
        for (File listFile : listFiles) {
            if(listFile.isDirectory()) continue;
            log.info("process file:"+listFile.getAbsolutePath());
            List<FaceFeature> faceFeatureList;
            boolean isVideo = listFile.getName().contains("mp4");
            if(isVideo){
                faceFeatureList = faceEngine.allFaceFeatureVideo(listFile);
            }else {
                faceFeatureList = faceEngine.allFaceFeatureImage(listFile);
            }
            for (FaceFeature faceFeature : faceFeatureList) {
                FaceRecordEntity faceRecordEntity=new FaceRecordEntity();
                faceRecordEntity.setFaceId(identifierGenerator.nextId(null));
                faceRecordEntity.setFileUrl(listFile.getAbsolutePath());
                faceRecordEntity.setRecordTime(LocalDateTime.now());
                faceRecordEntity.setFeature(Base64Utils.encodeToString(faceFeature.getFeature()));
                String featureMd5 = SecureUtil.md5(faceRecordEntity.getFeature());
                faceRecordEntity.setMd5(featureMd5);
                faceService.saveFace(faceRecordEntity);
            }
        }


    }



}
