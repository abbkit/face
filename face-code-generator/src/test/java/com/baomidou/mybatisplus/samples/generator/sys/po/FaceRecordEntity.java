package com.baomidou.mybatisplus.samples.generator.sys.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class FaceRecordEntity extends Model<FaceRecordEntity> {

      /**
     * 人脸唯一标识
     */
        @TableId(value = "faceId", type = IdType.ASSIGN_ID)
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


    @Override
    public Serializable pkVal() {
          return this.faceId;
      }

}
