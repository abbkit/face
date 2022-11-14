package com.abbkit.face.engine.impl.arcsoft;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

public class ArcSoftEngineImageWorker extends ArcSoftEngineWorker {

    private ReentrantLock lock=new ReentrantLock();

    public List<FaceFeature> syncFaceFeature(File file) throws Exception{

        try {
            lock.lock();
            //https://github.com/bytedeco/javacv
//            Mat mat = imread(file.getAbsolutePath());
//            int width = imageInfo.cols();
//            int height = mat.rows();

            ImageInfo imageInfo = getRGBData(file);
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            byte[] imageData = imageInfo.getImageData();
            //提取人脸信息
            List<FaceInfo> imageInfoList = new LinkedList<>();
            int errorCode = faceEngine.detectFaces(imageData, width, height, ImageFormat.CP_PAF_BGR24, imageInfoList);
            if (errorCode != ErrorInfo.MOK.getValue()) {
                throw new RuntimeException(""+errorCode);
            }

            if(imageInfoList.size()>0) {
                List<FaceFeature> faceFeatureList = new ArrayList<>();
                for (FaceInfo faceInfo : imageInfoList) {
                    FaceFeature faceFeature=new FaceFeature();
                    errorCode = faceEngine.extractFaceFeature(imageData, width, height, ImageFormat.CP_PAF_BGR24, faceInfo, faceFeature);
                    if (errorCode != ErrorInfo.MOK.getValue()) {
                        throw new RuntimeException(""+errorCode);
                    }
                    faceFeatureList.add(faceFeature);
                }
                return faceFeatureList;
            }
            return null;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void init(Key key,ArcSoftEngineConfiguration arcSoftEngineConfiguration,ArcSoftFunctionConfiguration arcSoftFunctionConfiguration) {

        String sdkKey=key.getSdkKey();
        String appId=key.getAppId();
        String sdkLibPath=key.getSdkLibPath();

        //指定引擎依赖库
        FaceEngine faceEngine = new FaceEngine(sdkLibPath);
        //激活引擎
        int errorCode = faceEngine.activeOnline(appId, sdkKey);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new RuntimeException("引擎激活失败");
        }
        //获取激活文件
        ActiveFileInfo activeFileInfo=new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new RuntimeException("获取激活文件信息失败");
        }
        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
//        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
//        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(32); //视频推荐16 图片推荐32
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new RuntimeException("初始化引擎失败");
        }

        this.faceEngine=faceEngine;
    }
}
