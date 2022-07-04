package iplay.cool.common;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author dove
 * @date 2022/5/14 17:07
 */
@Component
@Slf4j
public class LogIpPlugin extends ClassicConverter implements ApplicationContextAware {
    private static String ip = "0";

    @Override
    public String convert(ILoggingEvent event) {
        return ip;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InetAddress[] allByName = new InetAddress[0];
        try {
            allByName = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.error("LogIpPlugin ERROR");
        }
        Object[] array = Arrays.stream(allByName)
                .filter(inetAddress -> inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress())
                .map(InetAddress::getHostAddress).toArray();
        if (array.length > 0) {
            ip = array[0].toString();
        }
    }
}
