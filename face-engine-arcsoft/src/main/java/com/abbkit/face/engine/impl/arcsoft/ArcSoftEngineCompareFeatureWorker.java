package com.abbkit.face.engine.impl.arcsoft;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.CompareModel;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;

import java.util.concurrent.locks.ReentrantLock;

public class ArcSoftEngineCompareFeatureWorker extends ArcSoftEngineWorker {

    private ReentrantLock lock=new ReentrantLock();

    @Override
    public double compareFeature(FaceFeature source, FaceFeature target) throws Exception {
        try {
            lock.lock();
            FaceSimilar faceSimilar=new FaceSimilar();
            int errorCode=faceEngine.compareFaceFeature(target,source, CompareModel.ID_PHOTO,faceSimilar);
            if (errorCode != ErrorInfo.MOK.getValue()) {
                throw new RuntimeException(""+errorCode);
            }
            return faceSimilar.getScore();
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
