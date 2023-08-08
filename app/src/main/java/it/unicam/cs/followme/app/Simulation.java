package it.unicam.cs.followme.app;

import java.io.File;
import java.util.Scanner;

import it.unicam.cs.ragazzinipaul.api.model.dynamics.PrivateController;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.io.ShapeCreator;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.CommandsHandler;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.ragazzinipaul.api.io.SwarmController;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.CollisionController;
import it.unicam.cs.ragazzinipaul.api.io.RobotCreator;

/**
 * classe che solamente l'obbiettivo di adempiere alla richiesta di fornire una simulazione del sistema, uno pronto ed uno parametrizzabile,
 * ovviamente è possibile modificare le istruzioni relative ai comandi senza causare errori
 */
public class Simulation {
    private Environment environment;

    private ShapeCreator ShapeCreator;

    private CommandsHandler handler;

    private FollowMeParser parser;

    private SwarmController RobotController;

    private PrivateController PrivateCollisionController;

    private RobotCreator robotCreator;


    public Simulation(){
        this.environment= new Environment();

        this.ShapeCreator=new ShapeCreator<>(this.environment);

        this.handler=new CommandsHandler<>(this.environment);

        this.parser=new FollowMeParser(handler);

        this.RobotController=new SwarmController<>(parser,false);

        this.PrivateCollisionController=new PrivateController<>(this.environment,this.RobotController);

        this.RobotController.addPrivateCollisionController(this.PrivateCollisionController);

        this.robotCreator=new RobotCreator<>(this.environment);
    }


    private void DefaultSimulation() throws Exception {     //CASO 1 I ROBOT SI MUOVONO SEGUENDO UNO SPECIFICO MOVIMENTO
        //creazione di 5 robot con posizione randomica compresa tra 0,0 - 10,10
        this.robotCreator.create("bob");
        this.robotCreator.create("mike");
        this.robotCreator.create("liza");
        this.robotCreator.create("stephanie");


        this.RobotController.runCommandSet("MOVE 0.7 0.7 5");

        for(int i=0;i<10;i++){
         this.environment.printAllRobotPositions();
            Thread.sleep(1000);
        }

    }



    private void CreateSimulation() throws Exception { //CASO 2 SIMULAZIONE ENTRO UN TEMPO T
        String yellow = "\u001B[33m";
        String reset = "\u001B[0m";
        System.out.print(yellow + "QUANTI ROBOT VUOI CREARE ? ->" + reset);
        Scanner scanner = new Scanner(System.in);
        int robonumber = scanner.nextInt();

        char initial = 65;

        for (int i = 0; i < robonumber; i++) { //assegnazione automatica dell'ID al robot
            this.robotCreator.create(String.valueOf(initial));
            initial++;
        }


        this.ShapeCreator.parseAndCreate(
                "quadrato RECTANGLE 10 60 30 30\n" +  //TODO MODIFICARE A PIACERE
                        "cerchio CIRCLE 20 20 10\n" +
                        "rettangolo RECTANGLE 60 10 30 40\n");


        this.RobotController.runCommandSet(    //TODO MODIFICAR A PIACERE
                "MOVE 1 1 5\n" +
                        "STOP\n" +
                        "MOVE 1 1 5\n" +
                        "MOVE RANDOM 0 1 0 1 5\n" +
                        "SIGNAL LABEL\n" +
                        "STOP\n" +
                        "MOVE RANDOM 0 1 0.5 1 5\n"+
                        "FOLLOW LABEL 100 1\n");


        for (int i = 0; i < 20; i++) {
            this.environment.printAllRobotPositions();
            Thread.sleep(1000);
        }
    }





    public void choice() throws Exception {
        String yellow="\u001B[33m";
        String reset = "\u001B[0m";
        System.out.println(yellow+"INSERIRE 1 PER: 'I ROBOT SEGUONO TUTTI LO STESSO MOVIMENTO', 2 PER 'COMANDI ESEGUITI OGNI dT SECONDI' -> "+reset);
        int choice =0;
        Scanner scanner = new Scanner(System.in);
        choice=scanner.nextInt();
        if (choice == 1){this.DefaultSimulation();}else{this.CreateSimulation();}
    }


}
/* CONFIGURAZIONE

        Environment e = new Environment();

        ShapeCreator creator = new ShapeCreator<>(e);

        CommandsHandler handler = new CommandsHandler(e);

        FollowMeParser parser = new FollowMeParser(handler);

        SwarmController controller = new SwarmController<>(parser, false); o true per la feature aggiuntiva

        PrivateController private=new PrivateController<>(this.environment,this.RobotController);

        controller.addPrivateCollisionController(privateController);

        RobotCreator robotCreator = new RobotCreator(e);

 */

