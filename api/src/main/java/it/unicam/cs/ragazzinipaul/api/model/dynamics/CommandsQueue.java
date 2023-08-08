package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import it.unicam.cs.ragazzinipaul.api.utils.Pair;

/**
 * Implementazioni di quest'interfaccia permettono di immagazzinare queue di comandi ai fini dell'esecuzione di loop degli stessi.
 */
public interface CommandsQueue {

    /**
     * inserisce il comando alla coda della queue
     *
     * @param c    the comando
     * @param args the args
     * @throws InterruptedException the interrupted exception
     */
    void enqueue(String c,double[] args) throws InterruptedException;

    /**
     * rimuove l'elemento dalla testa della queue
     *
     * @return the pair alla testa della queue
     * @throws InterruptedException the interrupted exception
     */
    Pair dequeue() throws InterruptedException;

    /**
     * Delete data.
     */
    void deleteData();

}
