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

    /**
     * 获取视频中的所有人脸特征数据
     * @param file
     * @return
     * @throws Exception
     */
    List<FaceFeature> allFaceFeatureVideo(File file) throws Exception;


    void scanFaceFeatureVideo(File file,FaceFeatureListener listener) throws Exception;

    double compareFeature(FaceFeature source,FaceFeature target) throws Exception;
}
