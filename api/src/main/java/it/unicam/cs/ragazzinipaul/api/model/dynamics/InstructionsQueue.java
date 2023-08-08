package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * queue di comandi che eseguirà ogni ThreadedRobotMovement: ogni volta che un comando supera il parsing, viene inserito nella queue di istruzioni di ogni thread
 */
public class InstructionsQueue implements CommandUpdateQueue {
    private volatile BlockingQueue<double[]> instrucitions;

    /**
     * costruttore
     */
    public InstructionsQueue(){
        this.instrucitions=new LinkedBlockingQueue<>();
    }

    /**
     *
     * @return la testa della queue
     */
    public synchronized double[] dequeue() {

        return this.instrucitions.poll();

    }

    /**
     * aggiunge un array di valori alla coda della queue
     * @param args
     */
    public synchronized void enqueue(double[] args)  {

        this.instrucitions.add(args); //inserisce la mappa come nodo della queue
    }

    /**
     *
     * @return true se la queue è vuota, false altrimenti
     */
    public synchronized boolean isEmpty(){

        return this.instrucitions.isEmpty();
    }
}
