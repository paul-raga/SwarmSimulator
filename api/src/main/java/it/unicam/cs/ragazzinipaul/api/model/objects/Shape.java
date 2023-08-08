package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * Classe astratta utilizzata per sfruttare il polimorfismo nella classe Environment, fa da padre a tutti i tipi di shape bidimensionali
 *
 * @param <K> the type parameter label
 */
public abstract class Shape<K> implements ImmovableObject<K> {

    private double X;
    private double Y;
    public double getArea(){return 0;}
    public K getLabel(){return null;}

    public double getX(){return this.X;}
    public double getY(){return this.Y;}

    public boolean pointIsContained(double x, double y){return false;}


}