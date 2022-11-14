package com.abbkit.face.engine.impl.arcsoft;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({ArcSoftAutoConfig.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableArcSoft {


}
