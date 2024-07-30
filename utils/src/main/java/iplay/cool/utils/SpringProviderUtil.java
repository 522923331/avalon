//package iplay.cool.utils;
//
//import com.nal.common.core.domain.entity.SysUser;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author wu.dang
// * @since 2024/7/8
// */
//public class SpringProviderUtil {
//    private static final String REQUEST_USER = "nal-request-investor";
//
//    public static String getHeader(String key) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        return request.getHeader(key);
//    }
//
//    public static void bindSysUser(SysUser investor) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        request.setAttribute(REQUEST_USER, investor);
//    }
//
//    public static SysUser getSysUser() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        return (SysUser) request.getAttribute(REQUEST_USER);
//    }
//}
