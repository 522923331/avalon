package iplay.cool.pattern.chapter1;

/**
 * @author dove
 * @date 2022/7/3 0:38
 */
public class MuteQuack implements QuackBehavior{
    @Override
    public void quack() {
        //什么都不错，不会叫
        System.out.println("<<Silence>>");
    }
}
