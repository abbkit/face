package com.abbkit.face.engine.impl.arcsoft;

import com.arcsoft.face.FaceEngine;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

abstract public class ArcSoftEngineWorker {

    protected FaceEngine faceEngine;

    protected String id;

    /**
     * 每个引擎每次只能处理一个图片或者视频
     * @see ReentrantLock
     */
    protected final LinkedBlockingQueue queue=new LinkedBlockingQueue(1);

    protected final void lock(Object task) throws InterruptedException{
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            throw e;
        }
    }

    protected final void unlock(Object task) throws InterruptedException{
        queue.poll();
    }

    abstract public void init(Key key,ArcSoftEngineConfiguration arcSoftEngineConfiguration, ArcSoftFunctionConfiguration arcSoftFunctionConfiguration);



}
