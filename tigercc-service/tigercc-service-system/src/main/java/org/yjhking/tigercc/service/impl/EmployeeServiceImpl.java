package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.Employee;
import org.yjhking.tigercc.mapper.EmployeeMapper;
import org.yjhking.tigercc.service.IEmployeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
