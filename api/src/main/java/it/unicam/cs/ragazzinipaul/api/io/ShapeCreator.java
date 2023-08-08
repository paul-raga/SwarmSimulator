package it.unicam.cs.ragazzinipaul.api.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import it.unicam.cs.followme.utilities.*;

import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.model.objects.Shape;
import it.unicam.cs.ragazzinipaul.api.model.objects.Circle;
import it.unicam.cs.ragazzinipaul.api.model.objects.Rectangle;

/**
 * The type Shape creator.
 *
 * @param <T> the type parameter Shape
 * @param <K> the type parameterlabel
 */
public class ShapeCreator<T extends Shape, K> implements ImmovableObjectCreator{
    private Environment observer;  //ambiente osservatore

    private FollowMeParser parser;  //parser che verifica la correttezza delle shape passate come parametro

    private List<ShapeData> checkedShapes= new ArrayList<>();  //lista delle shape la cui correttezza grammaticale è stata accertata

    /**
     * Instantiates a new Shape creator.
     *
     * @param e the e
     */
    public ShapeCreator(Environment e){ //costruttore
        this.observer=e;
        this.parser=new FollowMeParser();
    }

    /**
     * Crea un shape di tipo Rettangolo e la aggiunge all'ambiente
     * @param label
     * @param x
     * @param y
     * @param width
     * @param higth
     * @return
     */
    private T createShape(K label, double x, double y, double width, double higth){   //creatore di istanze  RECTANGLE
        Rectangle new_rectangle = new Rectangle(x,y,width,higth,label);
        notify((T) new_rectangle,this.observer);
        return (T) new_rectangle;
    };

    /**
     * crea un shape di tipo Cerchio e la aggiunge all'ambiente
     * @param label
     * @param x
     * @param y
     * @param r
     * @return
     */
    private T createShape(K label, double x, double y, double r){ //creatore di istanza CIRCLE
        Circle new_circle = new Circle<>(x,y,r,label);
        notify((T) new_circle,this.observer);
        return (T) new_circle;
    };

    /**
     * notifica l'ambiente di aggiungere la nuova shape
     * @param shape
     * @param e
     */
    private void notify(T shape,Environment e){        //metodo che notifica l'ambiente dell'aggiunta di una shape
        e.addShape(shape);                            //l'ambiente aggiunge il nuovo Shape alla priopria List
    }

    /**
     * chiama il parser passandogli il codice da verificare
     * @param code
     * @throws FollowMeParserException
     */

    private void updateList(String code) throws FollowMeParserException { //chiama il parser passandogli il codice da verificare
        this.checkedShapes=this.parser.parseEnvironment(code);
    }
    /**
     * chiama il parser passandogli il codice da verificare
     * @param file
     * @throws FollowMeParserException
     */
    private void updateList(File file) throws FollowMeParserException, IOException { //chiama il parser passandogli il file da verificare
        this.checkedShapes=this.parser.parseEnvironment(file);
    }
    /**
     * chiama il parser passandogli il codice da verificare
     * @param path
     * @throws FollowMeParserException
     */
    private void updateList(Path path) throws FollowMeParserException, IOException { //chiama il parser passandogli il path del file da verificare
        this.checkedShapes=this.parser.parseEnvironment(path);
    }




    @Override
/**
 * Richiama il parsing di una stringa
 */
    public void parseAndCreate(String code) throws FollowMeParserException, InterruptedException {
        this.updateList(code);  //aggiorna la lista delle Shape verificate con quelle passategli dal parser
        Thread.sleep(10);   //do il tempo di aggiornare la lista in caso vengano passate molte shape
        this.instanciateShapes(); //istanzio tutte le shape
        this.checkedShapes=null; //svuoto la lista per evitare errori
    }

    /**
     * Richiama il parsing di una file
     * @param file
     * @throws FollowMeParserException
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void parseAndCreate(File file) throws FollowMeParserException, IOException, InterruptedException {   //chiama il parser passandogli il codice da verificare
        this.updateList(file);
        Thread.sleep(10);
        this.instanciateShapes();
        this.checkedShapes=null;
    }

    /**
     * Richiama il parsing di una file ad un certo path
     * @param path
     * @throws FollowMeParserException
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void parseAndCreate(Path path) throws FollowMeParserException, IOException, InterruptedException {   //chiama il parser passandogli il codice da verificare
        this.updateList(path);
        Thread.sleep(10);
        this.instanciateShapes();
        this.checkedShapes=null;
    }

    /**
     * istanzia le shape del cui codice è stato fatto il parsing
     */
    private void instanciateShapes(){  //metodo che istanzia tutte le shape della lista locale
        this.checkedShapes.forEach((shape) -> { //per ogni istanza di ShapeData nella list
                    if (shape.shape().equals("CIRCLE")) {
                        this.createShape((K) shape.label(),shape.args()[0],shape.args()[1],shape.args()[2]);  //se il type è CIRCLE istanzio un cerchio
                    }else{
                        if(shape.shape().equals("RECTANGLE")){ this.createShape((K) shape.label(),shape.args()[0],shape.args()[1],shape.args()[2],shape.args()[3]);}  //altrimenti un quadrato
                    }
                } //l'environment viene notificato automaticamente dai metodi di creazione
        );
    }



}
