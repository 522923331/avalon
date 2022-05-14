package iplay.cool.bo;

import lombok.Data;

/**
 * @author dove
 * @date 2022/5/14 15:44
 */
@Data
public class SysLogBO {
    private String className;

    private String methodName;

    private String params;

    private Long execTime;

    private String remark;

    private String createDate;

}
