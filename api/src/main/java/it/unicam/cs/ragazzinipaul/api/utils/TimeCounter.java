package it.unicam.cs.ragazzinipaul.api.utils;

import it.unicam.cs.ragazzinipaul.api.model.dynamics.ThreadedRobotMovement;

/**
 * Istanze di questa classe vengono create quando viene eseguito il comando CONTINUE n : viene creato un thread che allo scadere del tempo n blocca il THreadedRobotMovement a cui è stato
 * associato
 */
public class TimeCounter extends Thread{
    private int time;
    private ThreadedRobotMovement thread; //thread del robot in movimento da interrompere

    private volatile boolean flag=true;  //flag per stoppare l'esecuzione del run del timer

    /**
     * Instantiates a new Time counter.
     *
     * @param n        the n
     * @param movement il thread di movimento Associato
     */
    public TimeCounter(int n , ThreadedRobotMovement movement){
        this.time=n;
        this.thread=movement;
    }

    /**
     * fa partire il timer cha alla scadenza ferma il movimento del robot
     */
    @Override
    public void run(){

        while(flag) {

            try {
                this.sleep(this.time * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.thread.standBy();  //dopo un tempo uguale s secondi passati come parametro chiama callStop e ferma il thread di movimento

            this.flag=false;
       } //ferma il thread del timer


    }
}

