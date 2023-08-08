package it.unicam.cs.ragazzinipaul.api.model.dynamics;


import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.ragazzinipaul.api.utils.Position;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.io.SwarmController;
import it.unicam.cs.ragazzinipaul.api.model.objects.Shape;

/**
 * Istanze di questa classe controllano le collisioni tra robot e shape / tra tutti i robot e le shape .
 *
 * @param <T> the type parameter
 * @param <K> the type parameter
 * @param <Q> the type parameter
 */
public class CollisionController< T extends Robot, K extends Shape, Q extends Position> implements InteractionsController<T,Q,K>{

    /**
     * ho preferito creare un "immagine" locale della mappa dei robot e della lista delle shape per togliere un po' di carico di lavoro all'Environment
     * ed evitare rallentamenti all'aggiornamento delle posizioni in caso ci siano molti robot
     */
    private volatile Hashtable<T,Q> robotMap = new Hashtable<>();   //mappa dei robot locale , si aggiorna ad ogni chiamata dei metodi check / checkingJob

    private volatile List<K> shapeList;    //lista delle shape locale, si aggiorna ad ogni chiamata dei metodi check / checkingJob

    private Environment environment;  //ambiente condiviso da tutti

    private T robot;  //viene inizializzato in caso l'istanza sia un collisionController associato a threadedRobotMovement




    /**
     * Instantiates a new Collision Controller associato ad un solo robot di cui ne controlla il movimento.
     *
     * @param e l'Ambiente
     * @param robot il Robot
     */
    public CollisionController(Environment e, T robot){ //costruttore utilizzato per i collisionController associati ai thread di movimento
        this.environment=e;
        this.robot=robot;
    }

    /**
     * Controlla se il robot ha colliso con una Shape, in caso positivo notifica il Robot di segnalare la condizione
     */

    @Override
    public void check() {           //controlla le collisioni tra robot e forme

        this.updateAll(); //aggiorno lista delle Shape e mappa dei Robot
        double x=  this.robotMap.get(this.robot).getX();                       //creo un parametro x con la posizione
        double y=  this.robotMap.get(this.robot).getY();                       //creo un parametro y con la posizione

        for(int i=0;i<this.shapeList.size();i++){                        //per ogni shape nella lista

            if(this.shapeList.get(i).pointIsContained(x,y)){
                this.notifyRobot(this.robot, (String) this.shapeList.get(i).getLabel());      //se il robot è all'interno di una shape glielo faccio segnalare
            }
        }
    }


    /**
     * Notifica il robot di segnalare la collisione
     * @param robot il robot che dovrà segnalare
     * @param lable la label che dovrà essere segnalata
     */
    private void notifyRobot(Robot robot, String lable){ //notifica un robot di segnalare la collisione
        robot.signal(lable);
    }



    /**
     * Aggiorna le informazioni locali su Robot, posizione dei Robot e Shapes
     */
    private synchronized void updateAll(){   //aggiorna lista delle shape e mappa dei robot locali
        this.shapeList = this.environment.getShapesList();
        this.robotMap = this.environment.getAllRobots();}
}