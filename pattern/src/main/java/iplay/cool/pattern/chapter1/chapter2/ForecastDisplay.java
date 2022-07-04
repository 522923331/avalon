package iplay.cool.pattern.chapter1.chapter2;

/**
 * @author dove
 * @date 2022/7/3 20:26
 */
public class ForecastDisplay implements Observer,DisplayElement{

    public ForecastDisplay(Subject weatherData){
        weatherData.registerObservers(this);
    }

    @Override
    public void display() {
        System.out.println("雨天");
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        display();
    }
}
