package com.abbkit.face.engine.impl.arcsoft;

import com.abbkit.face.engine.FaceEngine;
import com.abbkit.face.engine.model.FaceFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
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

    @PostConstruct
    private void init(){
        log.info("init arcsoft engine....");
        ArcSoftEngineImageWorker imageWorker=new ArcSoftEngineImageWorker();
        imageWorker.init(key,engineConfiguration,functionConfiguration);
        this.imageWorker=imageWorker;

        ArcSoftEngineVideoWorker videoWorker=new ArcSoftEngineVideoWorker();
        videoWorker.init(key,engineConfiguration,functionConfiguration);
        this.videoWorker=videoWorker;
    }

    @Override
    public FaceFeature faceFeatureImage(File file) throws Exception {
        com.arcsoft.face.FaceFeature faceFeature = imageWorker.syncFaceFeature(file);
        FaceFeature feature=new FaceFeature();
        feature.setFeature(faceFeature.getFeatureData());
        return feature;
    }

    @Override
    public List<FaceFeature> faceFeatureListImage(File file) throws Exception {
        return null;
    }
}
