package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:18
 */
public class MallardDuck extends Duck{
    public MallardDuck(){
        quackBehavior = new Quack();
        flyBehavior = new FlyWithWings();
    }

    @Override
    public void display() {
        System.out.println("外观是绿色");
    }
}
