package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.MessageEmail;
import org.yjhking.tigercc.mapper.MessageEmailMapper;
import org.yjhking.tigercc.service.IMessageEmailService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-15
 */
@Service
public class MessageEmailServiceImpl extends ServiceImpl<MessageEmailMapper, MessageEmail> implements IMessageEmailService {

}
