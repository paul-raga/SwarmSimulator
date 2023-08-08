package it.unicam.cs.ragazzinipaul.api.io;

import java.io.File;
import java.nio.file.Path;
import it.unicam.cs.followme.utilities.FollowMeParser;

import it.unicam.cs.ragazzinipaul.api.model.dynamics.CollisionController;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.PrivateController;

/**
 * Istanze di questa classe vengono usate per applicare comandi allo sciame di robot  e agire in autonomia se di verifica un certa condizione (se la relativa flag è attiva)
 *
 *
 * @param <F> FollowMeParser
 */
public class SwarmController<F extends FollowMeParser> implements MotionController{

    private F parser;  //parser che farà il controllo dei comandi

    private PrivateController Controller;

    private boolean AdditionalFeature;  //variabile di attivazione della funzionalità di update che applica un comando in caso di collisioni con Shapes


    /**
     * Instantiates a new Swarm controller.
     *
     * @param parser the parser
     * @param choice the choice
     */
    public SwarmController(F parser,boolean choice) {
        this.parser = parser;
        this.AdditionalFeature=choice;

    }

    /**
     * esegue il parsing di una File di codice per poi eseguire i comandi
     * @param sourceFile the file
     * @throws Exception
     */
    @Override
    public void runCommandSet(File sourceFile) throws Exception {

        this.parser.parseRobotProgram(sourceFile);  //faccio partire il parser
        if(this.AdditionalFeature==true) {
            this.notifyController();     //notifico il collisionController che ho avviato una sequenza di comandi
        }
    }


    /**
     * esegue il parsing di una File di codice per poi eseguire i comandi
     * @param path the path
     * @throws Exception
     */
    @Override
    public void runCommandSet(Path path) throws Exception {

        this.parser.parseRobotProgram(path);   //faccio partire il parser
        if(this.AdditionalFeature==true) {
            this.notifyController();     //notifico il collisionController che ho avviato una sequenza di comandi
        }


    }


    /**
     * Fa il parsing di una stringa di codice per poi applicarne i comandi
     * @param code the command
     * @throws Exception
     */
    @Override
    public void runCommandSet(String code) throws Exception {

        this.parser.parseRobotProgram(code);  //faccio partire il parser

        if(this.AdditionalFeature==true) {
            this.notifyController();     //notifico il collisionController che ho avviato una sequenza di comandi
        }

    }

    /**
     * se lo SwarmController è associato ad un CollisionController, ad ogni set di comandi lo notifica di eseguire i controlli
     */
    private void notifyController()  {
        this.Controller.checkAndAct();       //chiamo il metodo del CollisionController per far partire un thread di Checking delle collisioni
    }

    /**
     * Comando lanciato dal CollisionController eventualmente Associato, applica un comando arbitrario in risposta ad una certa condizione percepita, in questo caso FOLLOW
     * @param lable the lable
     * @throws Exception
     */
    public synchronized void update(String lable) throws Exception {
        this.runCommandSet("STOP\n");          //se avviene una collisione tutti gli altri robot seguiranno questo comportamento
    }

    /**
     * Add collision controller.
     *
     * @param c the c
     */
    public void addPrivateCollisionController(PrivateController c){        //aggiungo un collisionController come observer degli spostamenti ad ogni set di comandi
        this.Controller=c;
    }


}

