package it.unicam.cs.ragazzinipaul.api.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import it.unicam.cs.followme.utilities.*;

/**
 * Classi che implementano quest interfaccia permettono l'istanziamento automatico di Shape una volta avvenuto il parsing relativo ai comandi di definizione dell'ambiente
 */
public interface ImmovableObjectCreator {
    /**
     * Fa il parsing e istanzia Shape
     *
     * @param s the S
     * @throws FollowMeParserException the follow me parser exception
     * @throws InterruptedException    the interrupted exception
     */

    /**
     * Fa il parsing e istanzia Shape a partire da una stringa di codici
     * @param s Stringa di comandi
     * @throws FollowMeParserException
     * @throws InterruptedException
     */
    void parseAndCreate(String s) throws FollowMeParserException, InterruptedException;

    /**
     * Fa il parsing e istanzia Shape a partire da una informazioni su un File
     * @param file
     * @throws FollowMeParserException
     * @throws IOException
     * @throws InterruptedException
     */
    void parseAndCreate(File file) throws FollowMeParserException, IOException, InterruptedException;

    /**
     * Fa il parsing e istanzia Shape a partire da un Path relativo a un file in cui si trovano informazioni
     * @param path
     * @throws FollowMeParserException
     * @throws IOException
     * @throws InterruptedException
     */
    void parseAndCreate(Path path) throws FollowMeParserException, IOException, InterruptedException;

}