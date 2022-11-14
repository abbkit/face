package com.abbkit.face.engine.impl.arcsoft;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.abbkit.face.arcsoft.engine")
public class ArcSoftEngineConfiguration {
}
