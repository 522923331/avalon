package iplay.cool.pattern.chapter1.chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author dove
 * @date 2022/7/3 19:51
 */
public class WeatherData2 extends Observable {
    private final List<Observer> observers = new ArrayList<>();
    private float temperature;
    private float humidity;
    private float pressure;



    public void measurementsChanged(){
        setChanged();
        notifyObservers();
    }

    public void setMeasurements(float temp,float humidity, float pressure){
        this.temperature = temp;
        this.humidity =humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}
