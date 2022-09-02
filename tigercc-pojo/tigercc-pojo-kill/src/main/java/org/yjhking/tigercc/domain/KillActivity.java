package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-09-01
 */
@TableName("t_kill_activity")
public class KillActivity extends Model<KillActivity> {
    public static final int STATUS_WAIT_PUBLISH = 0;
    public static final int STATUS_SUCCESS_PUBLISH = 1;
    public static final int STATUS_CANCEL_PUBLISH = 2;
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    /**
     * 字符串格式的开始时间
     */
    @TableField("time_str")
    private String timeStr;
    @TableField("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @TableField("end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 状态:
     * 0待发布 ,
     * 1已经发布,
     * 2取消发布 ,
     * 3秒杀结束(时间结束,没库存了)
     */
    @TableField("publish_status")
    private Integer publishStatus;
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 取消发布时间
     */
    @TableField("cancel_publish_time")
    private Date cancelPublishTime;
    @TableField("create_time")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getCancelPublishTime() {
        return cancelPublishTime;
    }

    public void setCancelPublishTime(Date cancelPublishTime) {
        this.cancelPublishTime = cancelPublishTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "KillActivity{" +
                ", id=" + id +
                ", name=" + name +
                ", timeStr=" + timeStr +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", publishStatus=" + publishStatus +
                ", publishTime=" + publishTime +
                ", cancelPublishTime=" + cancelPublishTime +
                ", createTime=" + createTime +
                "}";
    }
}
