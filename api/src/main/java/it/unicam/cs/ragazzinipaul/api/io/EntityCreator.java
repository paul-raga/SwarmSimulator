package it.unicam.cs.ragazzinipaul.api.io;

/**
 * Istanze di quest interfaccia creano istanze di entità in grado di segnalare la propria condizione (Robot)
 */
public interface EntityCreator {

    /**
     * Crea l'entità
     *
     * @param l'id dell'entità
     */
    void create(String id);

}
