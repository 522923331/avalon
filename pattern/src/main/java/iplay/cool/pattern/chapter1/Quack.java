package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:36
 */
public class Quack implements QuackBehavior{
    @Override
    public void quack() {
        System.out.println("鸭子呱呱叫");
    }
}
