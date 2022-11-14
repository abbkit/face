package com.abbkit.face.engine;

import com.abbkit.face.engine.model.FaceFeature;

import java.io.File;
import java.util.List;

public interface FaceEngine {

    /**
     * 根据图片获取人脸特征信息，图片中仅仅包含一个人脸
     * @param file
     * @return
     */
    FaceFeature faceFeatureImage(File file) throws Exception;

    /**
     * 根据图片获取人脸特征信息，图片中可能包含多个人脸
     * @param file
     * @return
     */
    List<FaceFeature> allFaceFeatureImage(File file) throws Exception;


}
