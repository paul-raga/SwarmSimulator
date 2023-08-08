package it.unicam.cs.ragazzinipaul.api.model.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import it.unicam.cs.ragazzinipaul.api.utils.Position;

/**
 * Classi che implementano quest interfaccia raccolgono i dati relativi a tutto ciò che è presente nell'ambiente
 * per fornirli alle classi che computano movimenti, collisioni ed eventuali rappresentazioni
 *
 * @param <P> parametro di tipo che estende Posizione
 * @param <S> parametro di tipo che estende Shape
 * @param <R> parametro di tipo che estende Robot
 */
public interface AllObjectsData <P extends Position,S extends Shape, R extends Robot>{


    /**
     * Aggiunge un robot all'ambiente.
     *
     * @param robot    il robot
     * @param position la posizione del robot
     */
    void addRobots(R robot, P position);


    /**
     * Aggiorna la posizione di un robot.
     *
     * @param robot    the robot
     * @param position the position
     */
    void updateRobots(R robot, P position);


    /**
     * Ritorna la mappatura in forma di HashTable dei robot  Key: robot Value:position.
     *
     * @return the all robots
     */
    Hashtable<R,P> getAllRobots();


    /**
     * Ritorna una lista di tutti i robot presenti nell'ambiente.
     *
     * @return the robo list
     */
    ArrayList<R> getRoboList();


    /**
     * Ritorna la posizione di un robot.
     *
     * @param robot the robot
     * @return the robot position
     */
    Position getRobotPosition(R robot);


    /**
     * Ritorna una mappa dei robot che stanno segnalando una condizione.
     *
     * @return the signaling robots
     */
    HashMap<R,P> getSignalingRobots();


    /**
     * Aggiunge una Shape all'ambiente.
     *
     * @param shape the shape
     */
    void addShape(S shape);


    /**
     * Ritorna una lista di tutte le shape nell'ambiente.
     *
     * @return the shapes list
     */
    List<S> getShapesList();


    /**
     * Ritorna una shape tramite le sue coordinate.
     *
     * @param x the x
     * @param y the y
     * @return the shape
     */
    S getShape(double x, double y);
}
