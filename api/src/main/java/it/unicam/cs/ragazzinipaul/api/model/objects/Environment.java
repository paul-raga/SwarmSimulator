package it.unicam.cs.ragazzinipaul.api.model.objects;

import java.util.*;
import it.unicam.cs.ragazzinipaul.api.utils.Position;

/**
 * Ambiente condiviso dove vengono caricati aggiornamenti e letti valori della posizione degli oggetti
 * @param <P> extends Position
 * @param <S> extends Shape
 * @param <R> extends Robot
 */
public class Environment<P extends Position,S extends Shape, R extends Robot> implements AllObjectsData<P,S,R>{

    private volatile Hashtable<R,P> robotPosition = new Hashtable<>(); //mappa delle robot come chiave e posizione come valore  (HASHTABLE é THREAD SAFE)
    private volatile Hashtable<P,R> positionRobot = new Hashtable<>(); //mappa delle posizioni come chiave e dei robot come valore

    private volatile ArrayList<S> shapeList= new ArrayList<>();  //lista delle shape
    private volatile ArrayList<R> roboList= new ArrayList<>(); //lista dei robot

    /**
     *
     * @return HashTable key=robot, value=posizione
     */
    public synchronized Hashtable<R,P> getAllRobots(){ //ritorna una hashtable dei robot e delle loro posizioni
        return this.robotPosition;
    }  //ritorna la table dei robot e la loro posizione


    /**
     * stampa la posizione di un robot dato il suo ID
     * @param Id Id del robot
     */

    public synchronized void printRobotPosition(String Id){          //stampa la posizione di un robot dato il suo Id
        this.robotPosition.forEach((k,v)->{String test = (String) k.getID();
            if(Id==test){ System.out.print("sono " +Id+ ", " +"mi trovo a X: ");System.out.print(this.robotPosition.get(k).getX());
                System.out.print(", Y: "); System.out.println(this.robotPosition.get(k).getY());
            }
        });
    }


    /**
     *
     * @param x the x della shape
     * @param y the y della shape
     * @return la shape corrispondente alle coordinate parametro
     */
    public synchronized S getShape(double x, double y) {     //ritorna una shape date le sue coordinate
        for(int i=0;i<this.shapeList.size();i++){
            if(this.shapeList.get(i).getX() == x && this.shapeList.get(i).getY() == y){ return this.shapeList.get(i);}
        }
        throw new NoSuchElementException();
    }


    /**
     * aggiunge un robot all'ambiente, con una certa posizione
     * @param robot    il robot
     * @param position la posizione del robot
     */
    public synchronized void addRobots(R robot, P position){    //aggiorna la mappatura dei robot con i nuovi robot (aggiunge un nuovo robot)
        this.robotPosition.put(robot,position);
        this.positionRobot.put(position,robot);

        this.roboList.add(robot);      //aggiorna la lista dei robot
    };

    /**
     * aggiunge una shape all'ambiente
     * @param shape the shape
     */
    public synchronized void addShape(S shape){
        this.shapeList.add(shape);
    }   //aggiorna la lista delle shape con una nuova shape

    /**
     * aggiorna la posizione del robot e il robot nella mappa
     * @param robot    the robot
     * @param position the position
     */
    public synchronized void updateRobots(R robot, P position){     //aggiorna la mappatura dei robot con le nuove posizioni
        this.robotPosition.put(robot,position);
        this.positionRobot.put(position,robot);
    };

    /**
     *
     * @return la list delle shape dell'ambiente
     */
    public synchronized List<S> getShapesList(){
        return this.shapeList;
    }  //ritorna la lista delle shape

    /**
     *
     * @param robot the robot
     * @return la Position del robot passato come parametro
     */
    public synchronized Position getRobotPosition(R robot){  //ritorna la posizione di un robot passandolo come parametro
        return this.robotPosition.get(robot);
    }

    /**
     *
     * @return una HashMap di tutti i robot che stanno segnalando qualcosa
     */
    public synchronized HashMap<R,P> getSignalingRobots(){   //ritorna una hashmap di robot che stanno segnalando (hanno la relativa flag attiva)
        HashMap<R,P> signaling_robots= new HashMap<>();
        this.robotPosition.forEach((k,v)->{if(k.getIfSignaling()==true) signaling_robots.put(k,v);});
        return signaling_robots;
    }

    /**
     *
     * @return una ArrayList di tutti i robot
     */
    public synchronized ArrayList<R> getRoboList(){
        return this.roboList;
    }  //ritorna la lista dei robot


    /**
     * stampa tutte le shape
     */
    public void printAllShapes(){  //stampa tutte le shape presenti nell'ambiente
        this.shapeList.forEach(shape -> System.out.println(shape));
    }

    /**
     * stampa tutti i robot
     */
    public void printAllRobot(){
        this.roboList.forEach((robot) -> {System.out.println(robot.getID());});
    }  //stampa tutti i robot nell'ambiente

    /**
     * stampa l'Id di ogni robot e la sua posizione
     */
    public synchronized void printAllRobotPositions(){
        this.robotPosition.forEach((k,v) -> {System.out.println(k.getID() + " X: " + v.getX() + ", Y: " + v.getY());});
        System.out.println("______________________________________________");
    }

}
