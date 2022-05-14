package iplay.cool.controller;

import iplay.cool.annotation.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dove
 * @date 2022/5/14 16:09
 */
@RestController
public class LogTestController {

    @SysLog("测试日志")
    @GetMapping("/testLog")
    public String testLog(@RequestParam("name") String name){
        return name;
    }
}
