package com.sxfenglei;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.CvPoint;
import org.bytedeco.opencv.opencv_core.CvScalar;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.cvPoint;
import static org.bytedeco.opencv.global.opencv_core.cvScalar;
import static org.bytedeco.opencv.global.opencv_imgproc.cvFont;


public class AppDemo {
    public static void main(String[] args) {
        //从官网获取的key
        String appId = "Ef8buEGXw5LhdZYuuBbZEwecdLJGknWMYxKBCMdsMrvX";
        String sdkKey = "FgMgwLC3rdpxH1pBrZ8j4Y3MTfVqsStVgog92ute5Lw8";

        String sdkLibPath = "C:\\Users\\Administrator\\Desktop\\ArcSoft_ArcFace_Java_Windows_x64_V3.0\\libs\\WIN64";

        //指定引擎依赖库
        FaceEngine faceEngine = new FaceEngine(sdkLibPath);
        //激活引擎
        int errorCode = faceEngine.activeOnline(appId, sdkKey);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
            System.exit(2);
        }
        //获取激活文件
        ActiveFileInfo activeFileInfo=new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("获取激活文件信息失败");
            System.exit(2);
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
            System.out.println("初始化引擎失败");
            System.exit(2);
        }

        //本地摄像头视频进行人脸识别
        OpenCVFrameGrabber grabber = null; //帧抓取器
        try{
            //抓取摄像头
            grabber = new OpenCVFrameGrabber(0);
            // grabber.setFrameRate(24);
            grabber.setImageWidth(960);
            grabber.setImageHeight(540);
            grabber.start();

            //新建窗口
            CanvasFrame canvasFrame = new CanvasFrame("摄像头");
            canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            canvasFrame.setAlwaysOnTop(true);

            //转换
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

            Frame frame = null;

            while (true) {
                if (!canvasFrame.isDisplayable()) {
                    grabber.stop();
                    System.out.println("退出");
                    System.exit(2);
                }

//                 frame = grabber.grabImage();
                frame = grabber.grab();

                IplImage iplImage = converter.convert(frame);
                byte[] imageData = new byte[iplImage.imageSize()];
                iplImage.imageData().get(imageData);

                //提取人脸信息
                List<FaceInfo> imageInfoList = new LinkedList<>();
                errorCode = faceEngine.detectFaces(imageData, iplImage.width(), iplImage.height(), ImageFormat.CP_PAF_BGR24, imageInfoList);
                int faceNum = imageInfoList.size();
                if(faceNum > 0){
                    System.out.println("提取到"+faceNum+"个人脸");

                    //人脸属性检测
                    FunctionConfiguration configuration = new FunctionConfiguration();
                    configuration.setSupportAge(true);
                    configuration.setSupportGender(true);
                    errorCode = faceEngine.process(imageData, iplImage.width(), iplImage.height(), ImageFormat.CP_PAF_BGR24, imageInfoList, configuration);

                    //性别检测
                    List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
                    errorCode = faceEngine.getGender(genderInfoList);
                    //年龄检测
                    List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
                    errorCode = faceEngine.getAge(ageInfoList);

                    //人脸画框
                    for(int i = 0; i<imageInfoList.size(); i++){
                        int x = imageInfoList.get(i).getRect().getLeft();
                        int y = imageInfoList.get(i).getRect().getTop();
                        int xMax = imageInfoList.get(i).getRect().getRight();
                        int yMax = imageInfoList.get(i).getRect().getBottom();
                        int gender = genderInfoList.size()>0 ? genderInfoList.get(i).getGender() : -1;
                        int age = ageInfoList.size()>0 ? ageInfoList.get(i).getAge() : -1;
                        String info = gender + ":" + age;
                        CvScalar cvScalar = cvScalar(0,0,255,0);
                        CvPoint cvPoint =  cvPoint(x,y);
                        CvPoint cvPoint1 = cvPoint(xMax,yMax);
                        opencv_imgproc.cvRectangle(iplImage, cvPoint, cvPoint1,cvScalar,1,4,0);
                        //中文显示乱码不知道怎么办 有知道的请指教谢谢
                        opencv_imgproc.cvPutText(iplImage,info,cvPoint(xMax,y+10),cvFont(1.0),cvScalar);
                    }

                }else{
                    System.out.print("未检测到人脸 ");
                }

                //图片刷新到窗口
                frame = converter.convert(iplImage);
                canvasFrame.showImage(frame);

            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            if(null != grabber){
                try {
                    grabber.close();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

