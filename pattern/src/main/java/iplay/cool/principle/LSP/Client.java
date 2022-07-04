package iplay.cool.principle.LSP;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dove
 * @date 2022/7/1 0:12
 */
class MobilePhone{
    public void call(){
        System.out.println("用移动电话打电话");
    }
}

class HuaWei extends MobilePhone{
    public void unlock(){
        System.out.println("打电话之前先解锁");
    }

    @Override
    public void call() {
        System.out.println("华为手机打电话");
    }
}

class User{
    public void startCall(HuaWei huaWei){
        huaWei.unlock();
        huaWei.call();
    }
}

abstract class A{
    public abstract Map<String,String> m();
}

class B extends A{

    @Override
    public HashMap<String, String> m() {
        return null;
    }
}


public class Client  {
    public static void main(String[] args) {
        User user =new User();
        user.startCall(new HuaWei());
        user.startCall((HuaWei) new MobilePhone());
    }
}
