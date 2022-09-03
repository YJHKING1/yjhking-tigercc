package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yjhking.tigercc.constants.NumberConstants;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-09-01
 */
@TableName("t_kill_course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KillCourse extends Model<KillCourse> {
    public static final String ACTIVITY_ID = "activity_id";
    public static final String COURSE_ID = "course_id";
    private static final String beforeKill = "未开始";
    private static final String nowKill = "秒杀中";
    private static final String afterKill = "已结束";
    private static final String wrongKill = "出错惹 ＞﹏＜";
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 课程名字
     */
    @TableField("course_name")
    @NotEmpty(message = "课程名字不能为空")
    private String courseName;
    /**
     * 对应的课程ID
     */
    @TableField("course_id")
    private Long courseId;
    @TableField("kill_price")
    private BigDecimal killPrice;
    /**
     * 库存
     */
    @TableField("kill_count")
    private Integer killCount;
    /**
     * 每个人可以秒杀的数量,默认1
     */
    @TableField("kill_limit")
    private Integer killLimit;
    /**
     * 秒杀课程排序
     */
    @TableField("kill_sort")
    private Integer killSort;
    /**
     * 状态:
     * 0待发布 ,
     * 1已经发布,
     * 2取消发布 ,
     * 3秒杀结束(时间结束,没库存了)
     */
    @TableField("publish_status")
    private Integer publishStatus;
    @TableField("course_pic")
    private String coursePic;
    /**
     * 秒杀开始时间
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 发布到Redis的时间
     */
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 老师，用逗号隔开
     */
    @TableField("teacher_names")
    private String teacherNames;
    /**
     * 下线时间
     */
    @TableField("cancel_publish_time")
    private Date cancelPublishTime;
    @TableField("activity_id")
    private Long activityId;
    @TableField("time_str")
    private String timeStr;
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private Boolean killing;
    @TableField(exist = false)
    private String killStatusName;
    @TableField(exist = false)
    private Long timeDiffMill;
    @TableField(exist = false)
    private Boolean unbegin;
    @TableField(exist = false)
    private Date now = new Date();
    
    public boolean getKilling() {
        return now.after(getStartTime()) && now.before(getEndTime());
    }
    
    public String getKillStatusName() {
        if (getKilling()) return nowKill;
        else if (now.before(getStartTime())) return beforeKill;
        else if (now.after(getEndTime())) return afterKill;
        else return wrongKill;
    }
    
    public boolean getUnbegin() {
        return now.before(getStartTime());
    }
    
    public Long getTimeDiffMill() {
        if (getUnbegin()) return (getStartTime().getTime() - now.getTime()) / NumberConstants.THOUSAND;
        else if (getKilling()) return (getEndTime().getTime() - now.getTime()) / NumberConstants.THOUSAND;
        else return NumberConstants.ZERO_LONG;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
