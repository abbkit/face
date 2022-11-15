package com.abbkit.face.service.model;

import com.abbkit.face.service.entity.FaceRecordEntity;
import lombok.Data;

@Data
public class FaceRecord extends FaceRecordEntity {

    private double score;

}
