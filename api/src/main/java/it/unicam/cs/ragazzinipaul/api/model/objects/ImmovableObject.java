package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * Classi che implementano questa interfaccia sono l'insieme degli oggetti inamovibili (Shape) dell'ambiente le cui label vengono segnalate dalle entità (Robot) in caso di collisione.
 *
 * @param <K> the type parameter
 */
public interface ImmovableObject<K> {
    /**
     * @return l'area della Shape
     */
    double getArea();  //ritorna l'area della figura

    /**
     * @return la label della Shape
     */
    K getLabel();      //ritorna la label associata alla figura

    /**
     * @return la coordinata X della Shape
     */
    double getX();     //ritorna la coordinata x della figura

    /**
     * @return la coordinata Y della Shape
     */
    double getY();     //ritorna la coordinata y della figura

    /**
     *
     * @param x cooridinata x
     * @param y coordinata y
     * @return true se il punto passato è contenuto nella figura, false altrimenti
     */
    boolean pointIsContained(double x, double y);

}


