package iplay.cool.service.impl;

import iplay.cool.bo.SysLogBO;
import iplay.cool.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author dove
 * @date 2022/5/14 15:47
 */
@Slf4j
@Service
public class SysLogServiceImpl implements SysLogService {
    @Override
    public boolean save(SysLogBO sysLogBO) {
        // 这里就不做具体实现了
        log.info(sysLogBO.getParams());
        return true;
    }
}
