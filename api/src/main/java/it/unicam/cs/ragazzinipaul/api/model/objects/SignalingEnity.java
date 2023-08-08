package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * Classi che implementano quest'interfaccia sono entità (Robot) capaci di segnalare una propria condizione al resto del sistema quando si verificano determinate circostanze
 *
 * @param <L> the type parameter
 */
public interface SignalingEnity<L> {
    /**
     * Segnala la label
     *
     * @param label the label
     */
    void signal(L label);

    /**
     * Arresta il segnale
     */
    void stopSignal();

    /**
     * Ritorna true se l'entità sta segnalando , false altrimenti
     *
     * @return the if signaling
     */
    boolean getIfSignaling();

    /**
     * Ritorna la label che l'entità sta segnalando al momento
     *
     * @return the sinal
     */
    String getSinal();
}