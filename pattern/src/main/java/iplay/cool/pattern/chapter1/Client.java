package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:41
 */
public class Client {
    public static void main(String[] args) {
        Duck duck = new ModelDuck();
        duck.performQuack();
        duck.setFlyBehavior(new FlyRocketPowered());
        duck.performFly();

    }
}
