package com.abbkit.face.service;

import com.abbkit.face.service.entity.FaceRecordEntity;
import com.abbkit.face.service.mapper.FaceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaceService {

    @Autowired
    private FaceRecordMapper faceRecordMapper;

    @Transactional
    public void saveFace(FaceRecordEntity faceRecordEntity){

        faceRecordMapper.insert(faceRecordEntity);

    }


}
