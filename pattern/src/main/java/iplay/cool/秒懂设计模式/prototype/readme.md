构造一个对象的过程是耗时耗力的。拿打印和复印来说，为了节省成本，通常会用打印机把电子文档打印到A4纸上（原型实例化过程），然后再用复印机把这份纸质文稿复制多份（原型拷贝过程）。
游戏卡顿，可能就跟对象的创建方式相关。new 对象的方式本身就很耗费CPU资源，在大型游戏中new的方式问题更为突出，很容易造成游戏卡顿。
深拷贝和浅拷贝
Java中的变量分为原始类型和引用类型，所谓浅拷贝是指只复制原始类型的值，比如横坐标x与纵坐标y这种以原始类型int定义的值，它们会被复制到新克隆出的对象中。
而引用类型bullet同样会被拷贝，但是请注意这个操作只是拷贝了地址引用（指针），也就是说副本敌机与原型敌机中的子弹是同一颗，因为两个同样的地址实际指向的内存对象是同一个bullet对象。

Java中的变量分为原始类型和引用类型，所谓浅拷贝是指只复制原始类型的值，比如横坐标x与纵坐标y这种以原始类型int定义的值，它们会被复制到新克隆出的对象中。而引用类型bullet同样会被拷贝，但是请注意这个操作只是拷贝了地址引用（指针），
也就是说副本敌机与原型敌机中的子弹是同一颗，因为两个同样的地址实际指向的内存对象是同一个bullet对象。
```java
public class EnemyPlane implements Cloneable{
    //Bullet也需要实现Cloneable接口
    private Bullet bullet;
    private int x;
    private int y;

    public EnemyPlane(int x,Bullet bullet){
        this.x = x;
        this.bullet = bullet;
    }

    //此处开放出set方法，方便调用者修改x坐标，为了保证克隆飞机的坐标位置个性化
    public void setX(int x){
        this.x = x;
    }
    
    public void setBullet(Bullet bullet){
        this.bullet = bullet;
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
        //克隆敌机
        EnemyPlane clonePlane =(EnemyPlane)super.clone();
        //对子弹进行深拷贝
        clonePlane.setBullet(this.bullet.clone());
        return clonePlane;
    }
}
```