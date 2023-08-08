package it.unicam.cs.followme.utilities;



public interface FollowMeParserHandler<T extends Object> {


    /**
     * Method invoked when a command "MOVE" is parsed.
     *
     * @param args argomenti del comando.
     */
    void moveCommand(double[] args) throws Exception;

    /**
     * Method invoked when a command "MOVE RANDOM" is parsed.
     *
     * @param args argomenti del comando.
     */
    void moveRandomCommand(double[] args) throws Exception;

    /**
     * Method invoked when a command "SIGNAL" is parsed.
     *
     * @param label label to signal
     */
    void signalCommand(String label) throws Exception;

    /**
     * Method invoked when a command "UNSIGNAL" is parsed.
     *
     * @param label label to unsignal
     */
    void unsignalCommand(String label) throws Exception;

    /**
     * Method invoked when a command "FOLLOW" is parsed.
     *
     * @param label label to follow
     * @param args  command arguments
     */
    void followCommand(String label, double[] args) throws Exception;

    /**
     * Method invoked when a command "STOP" is parsed.
     */
    void stopCommand() throws Exception;

    /**
     * Method invoked when a command "WAIT" is parsed.
     *
     * @    param s number of seconds;
     */
    void continueCommand(int s) throws Exception;

}