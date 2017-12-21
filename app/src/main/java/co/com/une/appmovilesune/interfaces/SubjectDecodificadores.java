package co.com.une.appmovilesune.interfaces;

/**
 * Created by davids on 25/10/16.
 */

public interface SubjectDecodificadores {

    void addObserverDecodificadores(ObserverDecodificadores o);

    void removeObserverDecodificadores(ObserverDecodificadores o);

    void notifyObserverDecodificadores();

}
