package com.abbkit.face.service;

import cn.hutool.core.bean.BeanUtil;
import com.abbkit.face.engine.FaceFeatureCompareService;
import com.abbkit.face.engine.model.FaceScore;
import com.abbkit.face.service.entity.FaceRecordEntity;
import com.abbkit.face.service.mapper.FaceRecordMapper;
import com.abbkit.face.service.model.FaceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FaceService {

    @Autowired
    private FaceRecordMapper faceRecordMapper;

    @Autowired
    private FaceFeatureCompareService faceFeatureCompareService;

    @Transactional
    public void saveFace(FaceRecordEntity faceRecordEntity){

        faceRecordMapper.insert(faceRecordEntity);

    }

    public List<FaceRecord> top(File file){
        try {
            List<FaceScore> scoreList = faceFeatureCompareService.top(file, 10);
            List<FaceRecord> faceRecordList=new ArrayList<>();
            for (FaceScore faceScore : scoreList) {
                long faceId = faceScore.getFaceId();
                FaceRecordEntity faceRecordEntity = faceRecordMapper.selectById(faceId);
                FaceRecord faceRecord= BeanUtil.copyProperties(faceRecordEntity,FaceRecord.class);
                faceRecord.setScore(faceScore.getScore());
                faceRecordList.add(faceRecord);
            }
            return faceRecordList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
