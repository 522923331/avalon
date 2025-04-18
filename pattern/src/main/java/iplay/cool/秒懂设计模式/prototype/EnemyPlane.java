package iplay.cool.秒懂设计模式.prototype;

import lombok.Getter;

/**
 * 原型模式，实现Cloneable接口，重写clone方法
 * @author wu.dang
 * @date 2025/4/18 13:18
 */
public class EnemyPlane implements Cloneable{
    private int x;
    private int y;

    public EnemyPlane(int x){
        this.x = x;
    }

    //此处开放出set方法，方便调用者修改x坐标，为了保证克隆飞机的坐标位置个性化
    public void setX(int x){
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void fly(){
        y++;
    }

    @Override
    protected EnemyPlane clone() throws CloneNotSupportedException {
        return (EnemyPlane)super.clone();
    }
}
