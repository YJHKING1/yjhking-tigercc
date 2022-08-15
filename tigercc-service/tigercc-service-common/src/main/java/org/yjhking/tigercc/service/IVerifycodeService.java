package org.yjhking.tigercc.service;

import org.yjhking.tigercc.dto.MobileCodeDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
public interface IVerifycodeService {
    JsonResult sendSmsCode(MobileCodeDto mobileCodeDto);
}
