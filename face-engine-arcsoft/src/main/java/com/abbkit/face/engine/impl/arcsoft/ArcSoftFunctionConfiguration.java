package com.abbkit.face.engine.impl.arcsoft;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.abbkit.face.arcsoft.func")
public class ArcSoftFunctionConfiguration {

    private Boolean supportFaceDetect=true;
    private Boolean supportFaceRecognition=true;
    private Boolean supportAge=true;
    private Boolean supportGender=true;
    private Boolean supportFace3dAngle=true;
    private Boolean supportLiveness=true;
    private Boolean supportIRLiveness=true;


}
