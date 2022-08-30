package org.yjhking.tigercc.service;

import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.dto.PayParamDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
public interface PayService {
    JsonResult apply(PayParamDto dto);
    
    String alipayNotify(AlipayNotifyDto dto);
}
