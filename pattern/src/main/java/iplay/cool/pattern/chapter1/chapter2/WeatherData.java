package iplay.cool.pattern.chapter1.chapter2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dove
 * @date 2022/7/3 19:51
 */
public class WeatherData implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private float temperature;
    private float humidity;
    private float pressure;

    @Override
    public void registerObservers(Observer observer) {
        observers.add(observer);
    }

    @Override
    public Observer removeObserver(Observer observer) {
        observers.remove(observer);
        return observer;
    }

    @Override
    public void notifyObservers() {
        observers.forEach(e -> e.update(temperature,humidity,pressure));
    }


    public void measurementsChanged(){
        notifyObservers();
    }

    public void setMeasurements(float temp,float humidity, float pressure){
        this.temperature = temp;
        this.humidity =humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

}
