package com.abbkit.face.engine.impl.arcsoft;

import org.springframework.context.annotation.Bean;

public class ArcSoftAutoConfig {

    @Bean
    public ArcSoftEngine arcSoftEngine(){
        ArcSoftEngine arcSoftEngine=new ArcSoftEngine();
        return arcSoftEngine;
    }

    @Bean
    public Key key(){
        return new Key();
    }

    @Bean
    public ArcSoftEngineConfiguration arcSoftEngineConfiguration(){
        return new ArcSoftEngineConfiguration();
    }

    @Bean
    public ArcSoftFunctionConfiguration arcSoftFunctionConfiguration(){
        return new ArcSoftFunctionConfiguration();
    }


}
