package com.abbkit.face.engine.impl.arcsoft;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.abbkit.face.arcsoft.key")
public class Key {


    private String appId = "Ef8buEGXw5LhdZYuuBbZEwecdLJGknWMYxKBCMdsMrvX";
    private String sdkKey = "FgMgwLC3rdpxH1pBrZ8j4Y3MTfVqsStVgog92ute5Lw8";

    private String sdkLibPath = "C:\\Users\\Administrator\\Desktop\\ArcSoft_ArcFace_Java_Windows_x64_V3.0\\libs\\WIN64";

}
