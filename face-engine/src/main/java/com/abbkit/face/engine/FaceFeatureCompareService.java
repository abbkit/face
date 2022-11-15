package com.abbkit.face.engine;

import com.abbkit.face.engine.model.FaceFeature;
import com.abbkit.face.engine.model.FaceScore;

import java.io.File;
import java.util.List;

public interface FaceFeatureCompareService {
    void append(FaceFeature faceFeature);

    void remove(long faceId);

    List<FaceScore> top(FaceFeature feature, int top) throws Exception;

    List<FaceScore> top(File file, int top) throws Exception;
}
