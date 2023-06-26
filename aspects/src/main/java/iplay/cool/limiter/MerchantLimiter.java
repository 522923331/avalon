//package iplay.cool.limiter;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * <p>
// * 商家上链限流表
// * </p>
// *
// * @author 2304
// * @since 2023-05-16
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
////@TableName("merchant_limiter")
//public class MerchantLimiter implements Serializable {
//
//    private static final long serialVersionUID=1L;
//
//    /**
//     * 主键
//     */
////    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    /**
//     * 商编
//     */
//    private String merchantNo;
//
//    /**
//     * 每秒限流请求数
//     */
//    private Integer txLimit;
//
//    /**
//     * 每秒限流请求数
//     */
//    private Integer queryLimit;
//
//    /**
//     * 是否启用：0否1是
//     */
//    private Integer used;
//
//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
//    /**
//     * 修改时间
//     */
//    private LocalDateTime modifyTime;
//
//
//}
