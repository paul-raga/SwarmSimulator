package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * Istanze di questa classe costituiscono lo sciame
 *
 * @param <L> the type parameter lable
 */
public class Robot<L> implements SignalingEnity{

    /**
     * lo unique identifier associato ad ogni Robot
     */
    private String ID; //lo unique identifier del robot

    private volatile boolean is_signaling;

    private volatile L signaled_label=null;


    /**
     * Instantiates a new Robot.
     *
     * @param id the id
     */
    public Robot(String id){
        this.ID=id;
        this.is_signaling=false;
    }

    /**
     * il robot inizia a segnalare la label passata come parametro
     * @param label the label
     */
    @Override
    public void signal(Object label) {
        this.is_signaling=true;
        this.signaled_label= (L) label;
        System.out.println("Sono Robot "+ this.ID + ", Segnalo: "+ label);

    }

    /**
     * il robot smette di segnalare
     */
    public void stopSignal(){
        this.is_signaling=false;
        this.signaled_label=null;
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getID(){
        return this.ID;
    }

    /**
     *
     * @return true se il robot sta segnalando
     */
    public boolean getIfSignaling(){
        if (this.is_signaling==true){return true;} else {return false;}
    }

    /**
     *
     * @return la label che il robot sta segnalando
     */
    public String getSinal(){
        return (String) this.signaled_label;
    }
}