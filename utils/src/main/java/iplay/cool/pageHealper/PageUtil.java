package iplay.cool.pageHealper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 *
 * @author wu.dang
 */
public class PageUtil extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage(PageParam page) {
        if (page == null) {
            throw new IllegalArgumentException("error page arguments");
        }
        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        if (pageNum != null && pageSize != null) {
            // 开始分页后底层放到了 thread local 里
            PageHelper.startPage(pageNum, pageSize).setReasonable(true);
        }
    }

    public static <T> PageInfo<T> getPageInfo(List<T> list) {
        return new PageInfo<>(list);
    }

    public static final <T> PageInfo<T> pageInfo(List<T> tList, PageParam param, Long total) {
        PageInfo<T> tPageInfo = new PageInfo<>(tList);
        tPageInfo.setTotal(total);
        tPageInfo.setPageNum(param.getPageNum());
        tPageInfo.setPageSize(param.getPageSize());
        return tPageInfo;
    }

    public static final <T> PageInfo<T> pageInfo(List<T> tList, PageParam param, Long total, int navigatePages) {
        PageInfo<T> tPageInfo = new PageInfo<>(tList);
        tPageInfo.setTotal(total);
        tPageInfo.setPageNum(param.getPageNum());
        tPageInfo.setPageSize(param.getPageSize());
        tPageInfo.setNavigatePages(navigatePages);
        return tPageInfo;
    }
}
