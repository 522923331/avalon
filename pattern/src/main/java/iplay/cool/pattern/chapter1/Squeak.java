package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:37
 */
public class Squeak implements QuackBehavior{
    @Override
    public void quack() {
        System.out.println("橡皮鸭吱吱叫");
    }
}
