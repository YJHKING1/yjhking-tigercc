package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.OperationLog;
import org.yjhking.tigercc.mapper.OperationLogMapper;
import org.yjhking.tigercc.service.IOperationLogService;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
