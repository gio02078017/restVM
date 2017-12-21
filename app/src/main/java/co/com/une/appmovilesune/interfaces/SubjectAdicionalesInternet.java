package co.com.une.appmovilesune.interfaces;

/**
 * Created by davids on 25/10/16.
 */

public interface SubjectAdicionalesInternet {

    void addObserverAdicionalesInternet(ObserverAdicionalesInternet o);

    void removeObserverAdicionalesInternet(ObserverAdicionalesInternet o);

    void notifyObserverAdicionalesInternet();

}
