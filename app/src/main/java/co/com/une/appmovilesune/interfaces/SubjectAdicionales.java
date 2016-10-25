package co.com.une.appmovilesune.interfaces;

/**
 * Created by davids on 25/10/16.
 */

public interface SubjectAdicionales {

    void addObserver(ObserverAdicionales o);

    void removeObserver(ObserverAdicionales o);

    void notifyObserver();

}
