package it.unicam.cs.ragazzinipaul.api.io;
import it.unicam.cs.ragazzinipaul.api.utils.Position;
import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;

/**
        * Permette di istanziare oggetti di tipo Robot o sue sottoclassi ed inserirle automaticamente nell'ambiente
        *
        *
        * @param <P> the type parameter Position
        */
public class RobotCreator<P extends Position> implements EntityCreator{

    /**
     * L'ambiente condiviso, viene aggiornato con l'aggiunta del nuovo robot
     */
    private Environment observer;                //Environment che osserva se nuovi robot vengono creati
    @Override
    public void create(String ID) {                   //crea un nuovo robot con un ID passatogli come parametro
        Robot new_robot= new Robot(ID);
        this.notify(new_robot, this.observer);    //chiama notify per dire all'ambiente che un nuovo robot è stato creato
    }

    /**
     * Instantiates a new Robot creator.
     *
     * @param e the e
     */
    public RobotCreator(Environment e){
        this.observer=e;
    }

    /**
     * Viene creata una posizione con coordinate randomiche
     * @return
     */
    private P assignPosition(){
        return  (P) new Position(Math.random() * 10,Math.random() * 10);  //genera un posizione randomica tra 0,0 e 10,10
    }

    /**
     * il robot e la posizione vengono aggiunte alla mappatura nell'ambiente
     * @param robot
     * @param e
     */
    private void notify(Robot robot, Environment e)     {   //notifica lo Swarm controller della creazione di un nuovo Robot
        P position = this.assignPosition();                 //viene generata una posizione randomica
        e.addRobots(robot,position);                        //chiama il metodo addRobot dell'environment per aggiungere il nuovo robot
    }
}
