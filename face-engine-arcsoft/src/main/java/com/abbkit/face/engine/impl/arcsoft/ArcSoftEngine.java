package com.abbkit.face.engine.impl.arcsoft;

import com.abbkit.face.engine.FaceEngine;
import com.abbkit.face.engine.FaceFeatureListener;
import com.abbkit.face.engine.model.FaceFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ArcSoftEngine implements FaceEngine {

    @Autowired
    private Key key;

    @Autowired
    private ArcSoftEngineConfiguration engineConfiguration;

    @Autowired
    private ArcSoftFunctionConfiguration functionConfiguration;

    private ArcSoftEngineImageWorker imageWorker;

    private ArcSoftEngineVideoWorker videoWorker;

    private ArcSoftEngineCompareFeatureWorker compareFeatureWorker;

    @PostConstruct
    private void init(){
        log.info("init arcsoft engine....");
        ArcSoftEngineImageWorker imageWorker=new ArcSoftEngineImageWorker();
        imageWorker.init(key,engineConfiguration,functionConfiguration);
        this.imageWorker=imageWorker;

        ArcSoftEngineVideoWorker videoWorker=new ArcSoftEngineVideoWorker();
        videoWorker.init(key,engineConfiguration,functionConfiguration);
        this.videoWorker=videoWorker;

        ArcSoftEngineCompareFeatureWorker compareFeatureWorker=new ArcSoftEngineCompareFeatureWorker();
        compareFeatureWorker.init(key,engineConfiguration,functionConfiguration);
        this.compareFeatureWorker=compareFeatureWorker;
    }

    @Override
    public FaceFeature faceFeatureImage(File file) throws Exception {
        List<com.arcsoft.face.FaceFeature> faceFeatureList = imageWorker.syncFaceFeature(file);
        FaceFeature feature=new FaceFeature();
        feature.setFeature(faceFeatureList.get(0).getFeatureData());
        return feature;
    }

    @Override
    public List<FaceFeature> allFaceFeatureImage(File file) throws Exception {
        List<com.arcsoft.face.FaceFeature> faceFeatureList = imageWorker.syncFaceFeature(file);
        List<FaceFeature> featureList=new ArrayList<>();
        for (com.arcsoft.face.FaceFeature faceFeature : faceFeatureList) {
            FaceFeature feature=new FaceFeature();
            feature.setFeature(faceFeature.getFeatureData());
            featureList.add(feature);
        }
        return featureList;
    }

    @Override
    public List<FaceFeature> allFaceFeatureVideo(File file) throws Exception {
        List<com.arcsoft.face.FaceFeature> faceFeatureList = videoWorker.syncFaceFeature(file);
        List<FaceFeature> featureList=new ArrayList<>();
        for (com.arcsoft.face.FaceFeature faceFeature : faceFeatureList) {
            FaceFeature feature=new FaceFeature();
            feature.setFeature(faceFeature.getFeatureData());
            featureList.add(feature);
        }
        return featureList;
    }

    @Override
    public void scanFaceFeatureVideo(File file, FaceFeatureListener listener) throws Exception {

    }

    @Override
    public double compareFeature(FaceFeature source, FaceFeature target) throws Exception {
        com.arcsoft.face.FaceFeature sourceFeature=new com.arcsoft.face.FaceFeature();
        sourceFeature.setFeatureData(source.getFeature());
        com.arcsoft.face.FaceFeature targetFeature=new com.arcsoft.face.FaceFeature();
        targetFeature.setFeatureData(target.getFeature());
        return compareFeatureWorker.compareFeature(sourceFeature,targetFeature);
    }
}
