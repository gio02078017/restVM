package co.com.une.appmovilesune.interfaces;

/**
 * Created by davids on 25/10/16.
 */

public interface SubjectTotales {

    void addObserver(ObserverTotales o);

    void removeObserver(ObserverTotales o);

    void notifyObserver();

}
