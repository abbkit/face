package com.abbkit.face.engine;

import com.abbkit.face.engine.model.FaceFeature;
import com.abbkit.face.engine.model.FaceScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Component
public class SimpleFaceFeatureCompareService implements FaceFeatureCompareService {


    @Autowired
    private FaceEngine faceEngine;

    private ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private List<FaceFeature> faceFeatureList=new ArrayList<>();

    @Override
    public void append(FaceFeature faceFeature){
        try {
            writeLock.lock();
            for (FaceFeature feature : faceFeatureList) {
                if(feature.getMd5().equals(faceFeature.getMd5())) return;
            }
            faceFeatureList.add(faceFeature);
        }finally {
            writeLock.unlock();
        }

    }

    @Override
    public void remove(long faceId){
        try {
            writeLock.lock();
            Iterator<FaceFeature> iterator = faceFeatureList.iterator();
            while (iterator.hasNext()){
                FaceFeature faceFeature = iterator.next();
                if(faceFeature.getFaceId()==faceId) {
                    iterator.remove();
                    return;
                }
            }
        }finally {
            writeLock.unlock();
        }

    }

    @Override
    public List<FaceScore> top(FaceFeature feature, int top)throws Exception{

        double minScore=0;
        List<FaceScore> faceScoreList=new LinkedList<>();
        try {
            readLock.lock();

            for (FaceFeature faceFeature : faceFeatureList) {
                double score;
                try {
                    score = faceEngine.compareFeature(feature, faceFeature);
                }catch (Exception e){
                    log.warn(e.getMessage());
                    score=0;
                }
                if(score>minScore){
                    FaceScore faceScore=new FaceScore();
                    faceScore.setFaceId(faceFeature.getFaceId());
                    faceScore.setScore(score);
                    faceScoreList.add(faceScore);
                }
            }

            Collections.sort(faceScoreList, new Comparator<FaceScore>() {
                @Override
                public int compare(FaceScore o1, FaceScore o2) {
                    return Double.valueOf(o2.getScore()).compareTo(Double.valueOf(o1.getScore()));
                }
            });

            return faceScoreList.subList(0,top>faceScoreList.size()?faceScoreList.size():top);
        }finally {
            readLock.unlock();
        }


    }

    @Override
    public List<FaceScore> top(File file, int top) throws Exception {

        FaceFeature faceFeature = faceEngine.faceFeatureImage(file);
        return top(faceFeature,top);
    }


}
