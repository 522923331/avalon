package iplay.cool.service;

import iplay.cool.bo.SysLogBO;

/**
 * @author dove
 * @date 2022/5/14 15:46
 */
public interface SysLogService {
    /**
    * 日志保存
    * @param sysLogBO 实体
    * @return boolean
    * @date 2022/5/14 15:46
    */
    boolean save(SysLogBO sysLogBO);
}
