package com.abbkit.face.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.abbkit.face.engine.FaceFeatureCompareService;
import com.abbkit.face.engine.model.FaceFeature;
import com.abbkit.face.service.entity.FaceRecordEntity;
import com.abbkit.face.service.mapper.FaceRecordMapper;
import com.abbkit.face.service.model.FaceRecord;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FaceFeatureLoaderTask {

    @Autowired
    private FaceRecordMapper faceRecordMapper;

    @Autowired
    private FaceFeatureCompareService faceFeatureCompareService;

    private volatile long maxFaceId=0;

    @Scheduled(cron="0/5 * * * * ?")
    public void scan() throws Exception {
        log.info("load face data from db.");
        List<FaceRecordEntity> faceRecordEntityList;
        do {
            LambdaQueryWrapper<FaceRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.gt(FaceRecordEntity::getFaceId, maxFaceId);
            queryWrapper.last("limit 0,5"); //last用法：在sql末尾添加sql语句，有sql注入风险
            faceRecordEntityList = faceRecordMapper.selectList(queryWrapper);
            for (FaceRecordEntity faceRecordEntity : faceRecordEntityList) {
                FaceFeature faceFeature = new FaceFeature();
                faceFeature.setFeature(Base64Utils.decodeFromString(faceRecordEntity.getFeature()));
                faceFeature.setFaceId(faceRecordEntity.getFaceId());
                faceFeature.setMd5(faceRecordEntity.getMd5());
                faceFeatureCompareService.append(faceFeature);
            }
            if(faceRecordEntityList.size()>0)
                maxFaceId=faceRecordEntityList.get(faceRecordEntityList.size()-1).getFaceId();
        }while (faceRecordEntityList.size()>0);
    }



}
