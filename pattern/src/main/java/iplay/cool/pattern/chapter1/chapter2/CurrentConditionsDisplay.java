package iplay.cool.pattern.chapter1.chapter2;

/**
 * @author dove
 * @date 2022/7/3 19:54
 */
public class CurrentConditionsDisplay implements Observer,DisplayElement{
    private float temperature;
    private float humidity;
    private float pressure;
    private Subject weatherData;

    public CurrentConditionsDisplay(Subject weatherData){
        this.weatherData = weatherData;
        weatherData.registerObservers(this);
    }

    public void cancelRegister(){
        weatherData.removeObserver(this);
    }

    @Override
    public void display() {
        System.out.println("当前温度为："+temperature+" 湿度为："+humidity+" 气压为:"+pressure);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        display();
    }
}
