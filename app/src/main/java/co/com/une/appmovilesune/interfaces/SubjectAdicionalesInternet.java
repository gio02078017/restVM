package co.com.une.appmovilesune.interfaces;

/**
 * Created by davids on 25/10/16.
 */

public interface SubjectAdicionalesInternet {

    void addObserverAdicionalesInternet(ObserverAdicionales o);

    void removeObserverAdicionalesInternet(ObserverAdicionales o);

    void notifyObserverAdicionalesInternet();

}
