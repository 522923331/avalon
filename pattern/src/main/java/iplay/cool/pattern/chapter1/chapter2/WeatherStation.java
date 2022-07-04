package iplay.cool.pattern.chapter1.chapter2;

/**
 * @author dove
 * @date 2022/7/3 20:29
 */
public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        Observer cur = new CurrentConditionsDisplay(weatherData);
        Observer stat = new StatisticDisplay(weatherData);
        Observer fore = new ForecastDisplay(weatherData);
        weatherData.setMeasurements(36.0f,50f,50);
    }
}
