package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:15
 */
public abstract class Duck {
    /**
    * 鸭子的行为委托给行为类
    * @date 2022/7/3 14:52
    */
    QuackBehavior quackBehavior;
    FlyBehavior flyBehavior;

    public void setQuackBehavior(QuackBehavior quackBehavior){
        this.quackBehavior = quackBehavior;
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void performQuack(){
        quackBehavior.quack();
    }
    public void performFly(){
        flyBehavior.fly();
    }
    public void swim(){
        System.out.println("鸭子在游泳");
    }
    public abstract void display();
}
