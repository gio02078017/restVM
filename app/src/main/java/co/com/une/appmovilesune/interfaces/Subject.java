package co.com.une.appmovilesune.interfaces;

public interface Subject {

    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObserver();

}
