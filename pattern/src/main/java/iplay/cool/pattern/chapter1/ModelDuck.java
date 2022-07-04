package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 14:59
 */
public class ModelDuck extends Duck {
    public ModelDuck(){
        quackBehavior = new Quack();
        flyBehavior = new FlyNoWay();
    }
    @Override
    public void display() {
        System.out.println("我一只模型鸭");
    }
}
