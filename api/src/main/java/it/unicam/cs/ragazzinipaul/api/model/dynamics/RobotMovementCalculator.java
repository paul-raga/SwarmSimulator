package it.unicam.cs.ragazzinipaul.api.model.dynamics;

/**
 * Classi che implementano quest'interfaccia calcolano la nuova posizione posizione di un Robot quando vengono applicati dei comandi di movimento
 */
public interface RobotMovementCalculator {

    /**
     * Gets thread name.
     *
     * @return the thread name
     */
    String getThreadName();

    /**
     * Call stop. Ferma il robot
     */
    void callStop();

    /**
     * Run. Istanze di quest interfaccia si intendono figlie della classe Thread, il corpo del calcolo è incluso in questo metodo
     */
    void run();

}
