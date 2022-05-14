package iplay.cool.config;

import iplay.cool.annotation.SysLog;
import iplay.cool.aspect.SysLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义starter配置类
 * 当环境中存在SysLog修饰的方法时，才加载这个类，做到动态插拔作用
 * @author dove
 * @date 2022/5/14 23:44
 */
@Configuration
@ConditionalOnBean(annotation = SysLog.class)
public class MyLogConfiguration {
    @Bean
    public SysLogAspect sysLogAspect(){
        return new SysLogAspect();
    }
}
