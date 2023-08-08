package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.utils.Position;

/**
 * Calcolatore della posizione di un robot a seguito di un comando, viene creato un thread che aggiorna la posizione del robot per simulare lo spostamento in tempo reale
 *
 * @param <R> the type parameter
 */
public class ThreadedRobotMovement<R extends Robot> extends Thread implements RobotMovementCalculator{

    private volatile double currentX=0;
    private volatile double currentY=0;
    private volatile double GoToX=0;
    private volatile double GoToY=0;
    private volatile double speed=0;

    private InstructionsQueue queue = new InstructionsQueue();

    private final String identifier; //identificatore di questo thread, uguale al nome del robot che prende come parametro

    private final Environment environment;  //Environment condiviso

    private final double timeConstant=1;   //aggiornamento della posizione ogni 1 secondi

    private final R robot; //robot associato al thread corrente, ovvero che si sta muovendo

    private final CollisionController collisionController;  //collision controller integrato al thread

    private volatile boolean stop=false; //flag per fermare il thread, se va a true il thread si arresta


    /**
     * Instantiates a new Threaded robot movement.
     *
     * @param robot the robot
     * @param e     the e
     * @param GoToX the go to x
     * @param GoToY the go to y
     * @param speed the speed
     */
    public ThreadedRobotMovement(R robot,Environment e, double GoToX, double GoToY, double speed){ //costruttore
        this.robot=robot;
        this.environment=e;

        this.currentX=this.environment.getRobotPosition(robot).getX();
        this.currentY= this.environment.getRobotPosition(robot).getY();

        this.identifier=robot.getID();

        this.GoToX=GoToX;
        this.GoToY=GoToY;

        this.speed=speed;

        this.collisionController=new CollisionController<>(this.environment,this.robot); //creo un collisionController usando come parametro l'ambiente condiviso e il robot associato a questo thread

    }

    /**
     * corpo del Thread, aggiorna la poszione richiamando il metodo newCoordinates ogni 1 secondi (variabile modificabile)
     */
    public void run() {

        while(!this.stop){

            this.dequeIfnotEmpty();

            Position newPosition = this.newCoordinates(this.currentX, this.currentY, this.currentX + this.GoToX, this.currentY + this.GoToY, this.timeConstant, this.speed);  //creo una nuova position calcolata da newCoordinates

            this.notify(this.robot, newPosition); //notifico l'Environment della modifica della posizione del robot associato a questo Thread

            this.currentX = newPosition.getX(); //aggiorno current X
            this.currentY = newPosition.getY(); //aggiorno current Y


            try {
                Thread.sleep(1000);   //lo sleep deve essere uguale alla timeConstant per garantire una posizione accurata in tempo reale
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.collisionController.check();  //chiama il collisionController per controllare se è avvenuta una collisione


        }
    }


    /**
     * Calcola e ritorna la nuova Posizione di un oggetto puntiforme che viaggia in moto lineare uniforme con i dati sottostanti
     * il metodo potrebbe essere più corto ma ho preferito scriverlo così per motivi di leggibilità ed evitare di commettere errori nei calcoli
     * @param startingX x iniziale
     * @param startingY y iniziale
     * @param finalX  x finale
     * @param finalY y finale
     * @param time tempo
     * @param speed velocità
     * @return new Position
     */
    private synchronized Position newCoordinates(double startingX, double startingY,double finalX, double finalY, double time, double speed){
        double posizione_x_iniziale=startingX;
        double posizione_y_iniziale=startingY;
        double posizione_x_destinazione=finalX;
        double posizione_y_destinazione=finalY;
        double tempo=time;
        double velocita=speed;

        double distanza_x = posizione_x_destinazione - posizione_x_iniziale;
        double distanza_y = posizione_y_destinazione - posizione_y_iniziale;

        double direzione_x = distanza_x / Math.sqrt(distanza_x * distanza_x + distanza_y * distanza_y);
        double direzione_y = distanza_y / Math.sqrt(distanza_x * distanza_x + distanza_y * distanza_y);

        double velocita_x = velocita * direzione_x;
        double velocita_y = velocita * direzione_y;

        double posizione_x = posizione_x_iniziale + velocita_x * tempo;
        double posizione_y = posizione_y_iniziale + velocita_y * tempo;

        return new Position(posizione_x,posizione_y);
    }


    /**
     * notifica l'ambiente della variazione di posizione del robot
     * @param r
     * @param p
     */
    private synchronized void notify(Robot r, Position p) {           //notifica l'Environment della modifica della posizione

        this.environment.updateRobots(r,p);

    }

    /**
     * ritorna l'Id del thread
     * @return
     */
    public String getThreadName(){
        return this.identifier;
    } //ritorna l'identifier del thread

    /**
     * ferma il thread tramite flag
     */
    public synchronized void callStop(){
        this.stop=true;
    }  //blocca l'esecuzione del thread


    /**
     * aggiorna destinazione e valocità quando viene applicato un nuovo comando
     *
     * @param newGox   the new gox
     * @param newGoY   the new go y
     * @param newspeed the newspeed
     */
    public synchronized void updateData( double newGox,  double newGoY, double newspeed) throws InterruptedException {  //aggiorna i dati di movimento in caso venga chiamato un altro comando di movimento


        double[] args= new double[3];

        args[0]=newGox;
        args[1]=newGoY;
        args[2]=newspeed;

        this.queue.enqueue(args);

    }

    /**
     * funzione invocata quando viene eseguito un comando STOP o CONTINUE, viene immesso in queue un comando con velocità 0 in modo che il robot stia fermo laddove si trova
     */
    public synchronized void standBy(){
        double[] args= new double[3];
        args[0]=this.GoToX;
        args[1]=this.GoToY;
        args[2]=0.0;
        this.queue.enqueue(args);
    }

    /**
     * se la queue di comandi non è vuota, rimuove l'elemento in testa e aggiorna
     */
    private void dequeIfnotEmpty(){
        if(!this.queue.isEmpty()){
            double newargs[]=this.queue.dequeue();
            this.GoToX=newargs[0];this.GoToY=newargs[1];this.speed=newargs[2];
        }
    }


}