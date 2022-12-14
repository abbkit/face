package com.abbkit.face.engine.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FaceFeature {

    private long faceId;

    private byte[] feature;

    private String md5;

}
