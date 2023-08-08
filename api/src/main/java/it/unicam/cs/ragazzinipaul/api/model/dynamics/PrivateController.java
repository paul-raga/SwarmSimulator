package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import it.unicam.cs.ragazzinipaul.api.io.SwarmController;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.ragazzinipaul.api.model.objects.Shape;
import it.unicam.cs.ragazzinipaul.api.utils.Position;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrivateController< T extends Robot, K extends Shape, Q extends Position>  implements CoupledCollisionController{

    private volatile Hashtable<T,Q> robotMap = new Hashtable<>();   //mappa dei robot locale , si aggiorna ad ogni chiamata dei metodi check / checkingJob

    private volatile List<K> shapeList;    //lista delle shape locale, si aggiorna ad ogni chiamata dei metodi check / checkingJob

    private Environment environment;  //ambiente condiviso da tutti

    private SwarmController observer; //viene inizializzato in caso l'instanza sia un CollisionController in comunicazione con lo SwarmController



    /**
     * Instantiates a new Collision Controller che lavora in concomitanza con lo SwarmController.
     *
     * @param e          l'Ambiente
     * @param controller lo SwarmController
     */
    public PrivateController(Environment e,SwarmController controller){  //costruttore utilizzato per il CollisionController in comunicazione con lo SwarmController
        this.environment=e;
        this.observer=controller;

    }

    /**
     * Controlla per tutti i robot se qualcuno ha colliso con una Shape, in caso positivo notifica il lo SwarmController di agire di conseguenza
     */
    @Override
    public void checkAndAct() {
        Thread checker = new Thread(this::checkingJob);  //fa partire un thread col compito di continuare a fare controlli
        checker.start();
    }


    /**
     * Avvia un thread che controlla costantemente la posizione dei Robot e vede se ci sono collisioni, in caso positivo notifica lo SwarmController
     */
    private void checkingJob() {
        AtomicBoolean flag= new AtomicBoolean(true);
        while (flag.get()) {

            this.updateAll();   //aggiorna mappa dei robot e lista delle shape

            this.robotMap.forEach((k, v) -> {                                 //per ogni robot
                double x = this.robotMap.get(k).getX();                       //creo un parametro x con la posizione
                double y = this.robotMap.get(k).getY();                       //creo un parametro y con la posizione

                for(int i=0;i<this.shapeList.size();i++){                        //per ogni shape nella lista
                    if (this.shapeList.get(i).pointIsContained(x,y)){
                        flag.set(false);
                        try {
                            this.notifySwarmController((String) this.shapeList.get(i).getLabel());  //se un robot è all'interno della Shape notifico lo swarmController
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    /**
     * Notifica lo SwarmController in caso di collisione
     * @param label la label della Shape che ha causato la collisione
     * @throws Exception
     */

    private void notifySwarmController(String label) throws Exception { //notifica lo swarmcontroller di agire in caso di collisione
        this.observer.update(label);
    }

    /**
     * Aggiorna le informazioni locali su Robot, posizione dei Robot e Shapes
     */
    private synchronized void updateAll(){   //aggiorna lista delle shape e mappa dei robot locali
        this.shapeList = this.environment.getShapesList();
        this.robotMap = this.environment.getAllRobots();}
}
