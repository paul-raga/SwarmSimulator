package it.unicam.cs.ragazzinipaul.api.utils;

/**
 * Classe di utility, ingloba una stringa che viene usata per invocare un comando nei loop nel FollowMeHandler insieme ai parametri di tale comando
 */
public class Pair implements CommandAndArgs{
    private String command;

    private double[] args;

    /**
     * Instantiates a new Pair.
     *
     * @param c the command
     * @param a the arguments
     */
    public Pair(String c, double[] a){
        this.command=c;
        this.args=a;
    }

    /**
     * Get command string.
     *
     * @return the string
     */
    public String getCommand(){
        return this.command;
    }

    /**
     * Get args double [ ].
     *
     * @return the double [ ]
     */
    public double[] getArgs(){
        return this.args;
    }
}

