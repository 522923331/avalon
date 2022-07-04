package iplay.cool.pattern.chapter1.chapter2;

/**
 * @author dove
 * @date 2022/7/3 19:41
 */
public interface Subject {

    void registerObservers(Observer observer);

    Observer removeObserver(Observer observer);

    void notifyObservers();
}
