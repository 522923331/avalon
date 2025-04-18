package iplay.cool.秒懂设计模式.prototype;

/**
 * 克隆工厂
 * @author wu.dang
 * @date 2025/4/18 13:24
 */
public class EnemyPlaneFactory {
    //创建敌机原型
    private static final EnemyPlane PROTOTYPE = new EnemyPlane(200);

    public static EnemyPlane getInstance(int x){
        try {
            EnemyPlane clone = PROTOTYPE.clone();
            clone.setX(x);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
