package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import it.unicam.cs.ragazzinipaul.api.utils.Pair;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Istanze di questa classe sono un metodo per immagazzinare i comandi nel corpo di un loop quando viene richiamato un comando del tipo : UNTIL, DO FOREVER, REPEAT
 * si una  queue thread-safe per rispettare l'ordine di esecuzione dei comandi nel corpo del loop.
 */
public class BufferedCommands implements CommandsQueue {


    private int counter=0;
    private BlockingQueue<Pair> commandQueue= new LinkedBlockingQueue<>();


    /**
     * Rimuove l'elemento Pair in testa alla lista
     * @return Pair (nome del comando e argomenti)
     * @throws InterruptedException
     */
    public synchronized Pair dequeue() throws InterruptedException {
        return this.commandQueue.take();

    }

    /**
     * Crea un istanza di Pair e la inserisce nella Queue
     * @param c il nome del comando
     * @param args gli argomenti del comando
     * @throws InterruptedException
     */
    public synchronized void enqueue(String c,double[] args) throws InterruptedException {

        Pair p = new Pair(c,args);  //crea un istanza di pair

        this.commandQueue.put(p); //inserisce la mappa come nodo della queue
        this.counter++;

    }

    /**
     * Cancella tutti gli elementi della Queue
     */
    public void deleteData(){
        this.commandQueue.clear();
    }

    /**
     * Stampa tutto quello che c'è nella queue
     */
    public void printAll(){
        this.commandQueue.forEach((command)-> {System.out.println(command);});
    }

    /**
     * Ritorna la dimensione della Queue
     *
     * @return the int
     */
    public int getSize(){
        return this.counter;
    }

}


