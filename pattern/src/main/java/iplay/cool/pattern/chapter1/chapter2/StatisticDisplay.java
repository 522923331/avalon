package iplay.cool.pattern.chapter1.chapter2;

/**
 * @author dove
 * @date 2022/7/3 20:25
 */
public class StatisticDisplay implements Observer,DisplayElement{
    private float temperature;
    private float humidity;
    private float pressure;
    //如果没有取消注册等功能的话，可以不添加这个变量
    private Subject weatherData;

    public StatisticDisplay(Subject weatherData){
        this.weatherData = weatherData;
        weatherData.registerObservers(this);
    }

    @Override
    public void display() {
        System.out.println("统计温度为"+temperature);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        display();
    }
}
