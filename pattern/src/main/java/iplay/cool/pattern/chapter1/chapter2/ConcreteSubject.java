package iplay.cool.pattern.chapter1.chapter2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dove
 * @date 2022/7/3 19:45
 */
public class ConcreteSubject implements Subject{
    private final List<Observer> observers = new ArrayList<>();
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

    }


}
