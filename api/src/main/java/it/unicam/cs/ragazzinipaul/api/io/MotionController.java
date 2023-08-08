package it.unicam.cs.ragazzinipaul.api.io;

import java.io.File;
import java.nio.file.Path;

/**
 * Classi che implementano quest'interfaccia permettono di applicare comandi sotto forma di File, Stringa Codice o Path verso file ad uno sciame di entità (Robot)
 * Tali classi richiamano le funzioni di Parsing della classe FollowMeParser che richiama le funzioni della classe FollowMeHandler
 * Vi è la funzionalità aggiuntiva di applicare comandi in automatico in risposta ad una collisione notificata da un MotionController associato
 */
public interface MotionController {

    /**
     * Viene effettuato il parsing e applicati i comandi sotto forma di Stringa
     *
     * @param command the command
     * @throws Exception the exception
     */
    void runCommandSet(String command) throws Exception;

    /**
     * Viene effettuato il parsing e applicati i comandi sotto forma di Stringa
     *
     * @param file the file
     * @throws Exception the exception
     */
    void runCommandSet(File file) throws Exception;

    /**
     * Viene effettuato il parsing e applicati i comandi sotto forma di Stringa
     *
     * @param path the path
     * @throws Exception the exception
     */
    void runCommandSet(Path path) throws Exception;

    /**
     * Viene utilizzato quando un istanza di questa classe è observer di un MotionController, innesca la reazione ad una collisione con una Shape di cui viene salvata la label
     *
     * @param lable the lable
     * @throws Exception the exception
     */
    void update(String lable) throws Exception;
}

