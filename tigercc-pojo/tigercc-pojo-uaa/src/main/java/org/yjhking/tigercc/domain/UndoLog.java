package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@TableName("undo_log")
public class UndoLog extends Model<UndoLog> {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("branch_id")
    private Long branchId;
    private String xid;
    private String context;
    @TableField("rollback_info")
    private Blob rollbackInfo;
    @TableField("log_status")
    private Integer logStatus;
    @TableField("log_created")
    private Date logCreated;
    @TableField("log_modified")
    private Date logModified;
    private String ext;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    
    public String getXid() {
        return xid;
    }
    
    public void setXid(String xid) {
        this.xid = xid;
    }
    
    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    public Blob getRollbackInfo() {
        return rollbackInfo;
    }
    
    public void setRollbackInfo(Blob rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
    }
    
    public Integer getLogStatus() {
        return logStatus;
    }
    
    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }
    
    public Date getLogCreated() {
        return logCreated;
    }
    
    public void setLogCreated(Date logCreated) {
        this.logCreated = logCreated;
    }
    
    public Date getLogModified() {
        return logModified;
    }
    
    public void setLogModified(Date logModified) {
        this.logModified = logModified;
    }
    
    public String getExt() {
        return ext;
    }
    
    public void setExt(String ext) {
        this.ext = ext;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "UndoLog{" +
                ", id=" + id +
                ", branchId=" + branchId +
                ", xid=" + xid +
                ", context=" + context +
                ", rollbackInfo=" + rollbackInfo +
                ", logStatus=" + logStatus +
                ", logCreated=" + logCreated +
                ", logModified=" + logModified +
                ", ext=" + ext +
                "}";
    }
}
