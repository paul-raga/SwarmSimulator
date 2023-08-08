package it.unicam.cs.ragazzinipaul.api.model.dynamics;

/**
 * implmentazioni di quest'interfaccia sono le queue di comandi che i ThreadedRobotMovement eseguiranno una volta terminato il parsing
 */
public interface CommandUpdateQueue {
    void enqueue(double[] args);

    double[] dequeue();
    boolean isEmpty();
}