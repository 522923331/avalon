package iplay.cool.pattern.chapter1.chapter2;

import java.util.Observable;
import java.util.Observer;

/**
 * @author dove
 * @date 2022/7/3 19:54
 */
public class CurrentConditionsDisplay2 implements Observer,DisplayElement{
    private float temperature;
    private float humidity;
    private float pressure;

    public CurrentConditionsDisplay2(Observable weatherData2){
        weatherData2.addObserver(this);
    }

    @Override
    public void display() {
        System.out.println("当前温度为："+temperature+" 湿度为："+humidity+" 气压为:"+pressure);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData2) {
            WeatherData2 weatherData2 = (WeatherData2)o;
            this.temperature = weatherData2.getTemperature();
            this.humidity = weatherData2.getHumidity();
            this.pressure = weatherData2.getPressure();
            display();
        }
    }
}
