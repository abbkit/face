package com.abbkit.face.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 人脸信息记录表
 * </p>
 *
 * @author J
 * @since 2022-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("face_record")
public class FaceRecordEntity {

      /**
     * 人脸唯一标识
     */
        @TableId(value = "faceId", type = IdType.INPUT)
      private Long faceId;

      /**
     * 特征数据的64位编码（byte编码）
     */
      @TableField("feature")
    private String feature;

      /**
     * 图片或者视频地址
     */
      @TableField("file_url")
    private String fileUrl;

      /**
     * 记录时间
     */
      @TableField("record_time")
    private LocalDateTime recordTime;

    /**
     * 特征数据的MD5值
     */
    @TableField("md5")
    private String md5;


}
