package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.utils.Position;
import it.unicam.cs.ragazzinipaul.api.utils.TimeCounter;
import it.unicam.cs.ragazzinipaul.api.utils.Pair;

/**
 * Handler dei comandi.
 *Questa classe si è ingrandita molto a causa di controlli all'interno dei metodi necessari per garantire un corretto funzionamento dei ThreadedRobotMovement,
 *ho preferito includere anche la possibilità di robot aggiunti a runtime e, anche qui, vi è una lista locale di robot che consente di non caricare troppo di
 * lavoro l'Environment ed evitare per quanto più possibile race conditions
 *
 * @param <R> istanza di Robot o classe figlia di Robot
 */
    public class CommandsHandler<R extends Robot> implements FollowMeParserHandler{


    private Environment environment;  //ambiente condiviso

    private volatile ArrayList<ThreadedRobotMovement> MovingRobots = new ArrayList<>();   //lista dei thread di movimento in esecuzione

    private volatile ArrayList<R> robots;  //array di tutti i robot sui quali applicare i comandi



    /**
     * Instantiates a new Commands handler.
     *
     * @param e the e
     */
    public CommandsHandler(Environment e){      //costruttore
        this.environment=e;
    }


    /**
     * Applica il comando MOVE ai robot istanziando un ThreadedRobotMovement per ciascuno, il thread viene poi aggiunto alla lista dei Thread attivi
     * Se ci sono ThreadedRobotMovement già attivi allora ne aggiorna le variabili
     * args[0]= X args[1]= Y args[2]=speed
     * @param args argomenti del comando.
     * @throws InterruptedException
     */
    @Override
    public void moveCommand(double[] args) throws InterruptedException {


            this.robots=environment.getRoboList(); //aggiorna la lista di robot locale

            this.MovingRobots.forEach((thread) -> {
                try {
                    thread.updateData(args[0],args[1],args[2]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }); // aggiorno i thread già in esecuzione con le nuove impostazioni

            this.robots.forEach((robot) ->  { //per ogni robot

                ThreadedRobotMovement movement=new ThreadedRobotMovement(robot,this.environment, args[0], args[1], args[2]);   //creo un istanza di Movement con i parametri del comando e il robot attualmente bufferizzato nel ciclo
                if(this.checkFlags(movement) == false){ //controllo che non esista un altro thread uguale associato al robot corrente
                    this.MovingRobots.add(movement); //se è un thread nuovo lo aggiungo alla lista dei thread
                    movement.start(); //lo faccio partire
                }
            });

        }

    /**
     * Applica il comando MOVE RANDOM instanziando un ThreadedRobotMovement per ciascun robot, il thread viene poi aggiunto alla lista dei thread attivi
     * Se ci sono ThreadedRobotMovement già attivi allora ne aggiorna le variabili
     * @param args argomenti del comando.    fromX= args[0],  toX=args[1],   fromY=args[2], toY=args[3],    speed = args[4]
     * @throws InterruptedException
     */
    @Override
    public void moveRandomCommand(double[] args) throws InterruptedException {



            this.robots=environment.getRoboList(); //aggiorna la lista dei robot

            this.MovingRobots.forEach((thread) -> {
                try {
                    thread.updateData(Math.random() * (args[1]- args[0]),Math.random() * (args[3]-args[2]),args[4]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }); // aggiorno i thread già in esecuzione con le nuove impostazioni

            this.robots.forEach((robot) ->{  //per ogni robot nell'environment
                double randomX=Math.random() * (args[1]-args[0]);   //creo la variabile X destinazione tramite la differenza tra i parametri
                double randomY=Math.random() * (args[3]-args[2]);   //creo la variabile Y destinazione tramite la differenza tra i parametri
                ThreadedRobotMovement movement=new ThreadedRobotMovement((Robot) robot,this.environment, randomX, randomY,  args[4]);   //creo un istanza di Movement passando come parametro il primo robot che viene rimosso dalla queue buffer
                if(this.checkFlags(movement) == false) { //controllo che non esista un altro thread uguale associato al robot corrente
                    this.MovingRobots.add(movement); //aggiungo il thread alla lista dei thread
                    movement.start();
                }
            });

        }

    /**
     * Fa segnalare a tutti i robot la label parametro
     * @param label label to signal
     * @throws InterruptedException
     */
    @Override
    public void signalCommand(String label) throws InterruptedException {

            this.robots=environment.getRoboList();
            this.robots.forEach((robot) -> {robot.signal(label);});    //per ogni robot gli faccio segnalare la label
        }

    /**
     * Fa smettere di segnalare la label parametro a tutti i robot
     * @param label label to unsignal
     * @throws InterruptedException
     */
    @Override
    public void unsignalCommand(String label) throws InterruptedException {

            this.robots=environment.getRoboList();
            this.robots.forEach((robot) -> {
                if(robot.getSinal().equals( label))robot.stopSignal();});  //per ogni robot gli faccio smettere di segnalare la labòe
        }


    /**
     * Istanzia per ogni robot un ThreadedRobotMovement in direzione dei robot che segnalano una certa label, il thread viene poi aggiunto alla lista dei thread attivi
     * Se ci sono ThreadedRobotMovement già attivi allora ne aggiorna le variabili
     * @param label label to follow
     * @param args  command arguments
     * @throws InterruptedException
     * dist= args[0], speed= args[1]
     */
    @Override
    public void followCommand(String label, double[] args) throws InterruptedException {


            Position to_be_followed= this.averagePosition(this.lableSignalingRobotsPositions(label,args[0])); //creo la posizione da seguire invocando i metodi opportuni
            if(to_be_followed.getX() != 0 && to_be_followed.getY()!=0){   //se to be follow non è 0

                this.robots=environment.getRoboList();  //aggiorno la list

                this.MovingRobots.forEach((thread) -> {
                    try {
                        thread.updateData(to_be_followed.getX(),to_be_followed.getY(),args[1]);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }); // aggiorno i thread già in esecuzione con le nuove impostazioni

                this.robots.forEach((robot) ->  {
                    ThreadedRobotMovement movement=new ThreadedRobotMovement((Robot) robot,this.environment, to_be_followed.getX(), to_be_followed.getY(), args[1]);   //creo un istanza di Movement passando come parametro il primo robot che viene rimosso dalla queue buffer
                    if(this.checkFlags(movement) == false){ //controllo che non esista un altro thread uguale associato al robot corrente
                        this.MovingRobots.add(movement); //aggiungo il thread alla lista dei thread
                        movement.start();}

                });}else{ double array[] = new double[5]; array[0]=-args[0];array[1]=args[0];array[2]=-args[0];array[3]=args[0];array[4]=args[1]; //se to_be_followd è 0,0 cioè se non ci sono robot
                this.moveCommand( array);  //richiamo moveRandom con i parametri dist e speed
            }
        }



    /**
     * Fa eseguire a tutti i thread la funzione di standBy, che li porta ad essere stazionari laddove si trovano
     * @throws InterruptedException
     */
    @Override
    public void stopCommand() throws InterruptedException {

        this.MovingRobots.forEach((thread) -> {thread.standBy();});

    }


    /**
     * Istanzia un oggetto della classe Timer che setta il thread a cui è associato in standBy dopo il tempo in secondi passato come parametro, fermando così il robot dopo un tempo T dalla chiamata del comando
     * @param s number of seconds;
     * @throws InterruptedException
     */
    @Override
    public void continueCommand(int s) throws InterruptedException {

            this.MovingRobots.forEach(
                    (thread) -> {   //per ogni thread di movimento in esecuzione
                        TimeCounter timer=new TimeCounter(s,thread);
                        timer.start();    //faccio partire un timer che alla scadenza ferma il thread
                    });
        }




    /**
     * calcola la distanza tra due punti
     * @param x1 x del primo punto
     * @param y1 y del primo punto
     * @param x2 x del secondo punto
     * @param y2 y del del secondo punto
     * @return la distanza tra i due punti
     */
    private double distanceBetweenPoints(double x1,double y1,double x2,double y2) {
        double distanza = 0;
        distanza = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distanza;
    }



    /**
     *
     * @param label
     * @param dist
     * @return la lista delle posizioni dei robot che segnalano label è che si trovano a distanza minore di dist
     */
    private ArrayList<Position> lableSignalingRobotsPositions(String label, double dist){

        HashMap<Robot,Position> signalers= this.environment.getSignalingRobots();  //mappa dei robot che segnalano qualcosa
        ArrayList<Position> positions=new ArrayList<>(); //arrayList delle posizioni dei robot che segnalano la lable
        signalers.forEach((k,v) -> {
            if (k.getSinal().equals(label)) positions.add(v); //se il robot segnala la label passata come parametro lo aggiungo nella list
        });

        positions.forEach((position) -> { //per ogni posizione nella List
            signalers.forEach((k,v)->{  //per ogni robot nella Map
                        if(distanceBetweenPoints(position.getX(),position.getY(),v.getX(),v.getY()) > dist)  //se la distanza tra i due punti è maggiore di dist parametro
                        {positions.remove(position);}  //rimuovo la posizione dalla lista
                    }
            );
        });
        return positions;  //ritorno la lista delle posizioni dei robot che segnalano label è che si trovano a distanza minore di dist
    }

    /**
     * data una lista di Position ritorna una Poision che è la media della distanza tra le Position
     * @param positions lista di Position
     * @return
     */
    private Position averagePosition (ArrayList<Position> positions){
        double averageX=0; //inizializzo x
        double averageY=0; //inizializzo y

        for(int i=0;i<positions.size();i++){ //per tutte le posizioni nella List
            averageX += positions.get(i).getX(); //sommo in x
            averageY += positions.get(i).getY(); //sommmo in y
        }

        averageX=averageX/positions.size(); //calcolo la media
        averageY=averageY/positions.size(); //calcolo la media

        Position follow_position = new Position(averageX,averageY); //creo la posizione da passare come parametro con le medie
        if(positions.isEmpty()){return new Position(0,0);}else{
            return  follow_position;}
    }

    /**
     * Ritorna il numero di thread in esecuzione, usato nei test
     */
    public synchronized int getAliveThreads(){ //stampa lo stato dei thread in esecuzione, usato per i test
        int count=0;

            for(int i=0;i<this.MovingRobots.size();i++){
            if(this.MovingRobots.get(i).isAlive()) count++;}

            return count;
    }

    /**
     * ferma l'esecuzione di tutti i ThreadedRobotMovment, usato nei test
     */
    public void stopAll(){
        this.MovingRobots.forEach(ThreadedRobotMovement::callStop);
    }

    /**
     * controlla se esiste già un thread uguale a quello passatogli come parametro nella lista dei Thread attivi
     * @param new_wannabe_thread ThreadedRobotMovement
     * @return
     */
    private boolean checkFlags(ThreadedRobotMovement new_wannabe_thread){
        boolean flag= false;
        for(int i=0;i<this.MovingRobots.size();i++){
            if(new_wannabe_thread.getThreadName().equals(this.MovingRobots.get(i).getThreadName())) {flag=true;}
        }
        return flag;
    }

    /**
     * metodo usato nei test
     * @return il numero di ThreadedRobotMovement attivi
     */
    public int getRunningThreadsNumber(){
        int counter=0;
        for (int i=0;i<this.MovingRobots.size();i++){
            if (this.MovingRobots.get(i).isAlive()) counter++;
        }
        return counter;
    }


}