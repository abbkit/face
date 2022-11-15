package com.abbkit.face.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.abbkit.face.service.model.FaceRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Slf4j
@Component
public class CompareImageTask {

    @Autowired
    private FaceService faceService;

    @Scheduled(cron="0/5 * * * * ?")
    public void scan() throws Exception {

        String fileDirPath = "D:\\face\\images\\in";
        log.info("scan file dir:"+fileDirPath);
        File dir=new File(fileDirPath);
        File[] listFiles = dir.listFiles();
        for (File listFile : listFiles) {
            if(listFile.isDirectory()) continue;
            if (!listFile.getName().contains("jpg")) continue;
            log.info("process file:"+listFile.getAbsolutePath());
            List<FaceRecord> faceRecordList = faceService.top(listFile);
            IoUtil.write(new FileOutputStream(fileDirPath+"\\"+listFile.getName()+".score"),true,
                    JSONUtil.toJsonStr(faceRecordList).getBytes());
        }
    }



}
