package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:35
 */
public class FlyNoWay implements FlyBehavior{
    @Override
    public void fly() {
        //什么都不做，不会飞
        System.out.println("i can't fly");
    }
}
