package com.abbkit.face.engine.impl.arcsoft;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.CvPoint;
import org.bytedeco.opencv.opencv_core.CvScalar;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.cvPoint;
import static org.bytedeco.opencv.global.opencv_core.cvScalar;
@Slf4j
public class ArcSoftEngineVideoWorker extends ArcSoftEngineWorker {


    @Override
    public List<FaceFeature> syncFaceFeature(File file) throws Exception {
        //本地摄像头视频进行人脸识别
        OpenCVFrameGrabber grabber = null; //帧抓取器
        try{
            //抓取摄像头
            grabber = new OpenCVFrameGrabber(file);
            // grabber.setFrameRate(24);
            grabber.setImageWidth(960);
            grabber.setImageHeight(540);
            grabber.start();

            //转换
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            List<FaceFeature> faceFeatureList=new ArrayList<>();
            int lengthInFrames = grabber.getLengthInFrames();
            for (int i = 0; i < lengthInFrames; i++) {
                Frame frame;
                try {
                    frame = grabber.grabFrame();
                }catch (Exception e){
                    log.error(e.getMessage()+"file:"+file.getAbsolutePath());
                    break;
                }
                IplImage iplImage = converter.convert(frame);
                byte[] imageData = new byte[iplImage.imageSize()];
                iplImage.imageData().get(imageData);
                //提取人脸信息
                List<FaceInfo> imageInfoList = new LinkedList<>();
                int errorCode = faceEngine.detectFaces(imageData, iplImage.width(), iplImage.height(), ImageFormat.CP_PAF_BGR24, imageInfoList);
                if (errorCode != ErrorInfo.MOK.getValue()) {
                    throw new RuntimeException(""+errorCode);
                }
                int faceNum = imageInfoList.size();
                if(faceNum > 0){
                    for (FaceInfo faceInfo : imageInfoList) {
                        FaceFeature faceFeature=new FaceFeature();
                        faceEngine.extractFaceFeature(imageData,iplImage.width(), iplImage.height(),
                                ImageFormat.CP_PAF_BGR24,faceInfo,faceFeature);
                        faceFeatureList.add(faceFeature);

                        // 绘制矩形框
                        int x = faceInfo.getRect().getLeft();
                        int y = faceInfo.getRect().getTop();
                        int xMax = faceInfo.getRect().getRight();
                        int yMax = faceInfo.getRect().getBottom();
                        CvScalar cvScalar = cvScalar(0,0,255,0);
                        CvPoint leftTop =  cvPoint(x,y);
                        CvPoint rightBottom = cvPoint(xMax,yMax);
                        opencv_imgproc.cvRectangle(iplImage, leftTop, rightBottom,cvScalar,1,4,0);

                        Frame outFrame = converter.convert(iplImage);
                        String imageMat="jpg";
                        String filePath="D:\\face\\images\\out\\"+ faceInfo.getFaceId()+"_"+i+"."+imageMat;
                        writeFrame2Image(outFrame,filePath,imageMat);

                    }

                }

            }

        }catch (Exception e){
            throw e;
        }finally {
            if(null != grabber){
                grabber.close();
            }
        }
        return Collections.emptyList();
    }

    private static void writeFrame2Image(Frame frame, String filePath, String imageMat)throws Exception {
        if ( frame == null || frame.image == null) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(frame);
        File output = new File(filePath);
        ImageIO.write(bi, imageMat, output);
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
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_VIDEO);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
//        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16); //视频推荐16 图片推荐32
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
