package com.abbkit.face.task;

import com.abbkit.face.engine.FaceEngine;
import com.abbkit.face.engine.model.FaceFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ScanImageTask {

    @Autowired
    private FaceEngine faceEngine;

    @Scheduled(cron="0/2 * * * * ?")
    public void scan() throws Exception {

        String fileDirPath = "D:\\face\\images";
        log.info("scan file dir:"+fileDirPath);
        File dir=new File(fileDirPath);
        File[] listFiles = dir.listFiles();
        for (File listFile : listFiles) {
            log.info("process file:"+listFile.getAbsolutePath());
            FaceFeature faceFeature = faceEngine.faceFeatureImage(listFile);
            log.info(listFile.getAbsolutePath()+">"+faceFeature.toString());
        }


    }



}
