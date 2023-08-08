package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * Istanze di questa classe sono delle forme di tipo Circle
 * Tale classe estende Shape
 *
 * @param <K> parametro di tipo della label della shape
 */
public class Circle<K> extends Shape implements ImmovableObject{
    /**
     * The X.
     */
    final double X;
    /**
     * The Y.
     */
    final double Y;

    /**
     * The Radius.
     */
    final double radius;
    /**
     * The Label.
     */
    final K label;

    /**
     * Instantiates a new Circle.
     *
     * @param x      the x
     * @param y      the y
     * @param radius the radius
     * @param label  the label
     */
    public Circle(double x, double y, double radius, K label) {
        this.X = x;
        this.Y = y;

        this.radius = radius;

        this.label = label;
    }

    /**
     *
     * @return l'area del cerchio
     */
    public double getArea() {
        return (this.radius * this.radius) * Math.PI;
    }

    /**
     *
     * @return la label del cerchio
     */
    public K getLabel() {
        return this.label;
    }

    /**
     *
     * @return la coordinata X del cerhio
     */
    public double getX() {
        return this.X;
    }

    /**
     *
     * @return la coordinata Y del cerchio
     */
    public double getY() {
        return this.Y;
    }

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     *
     * @param x cooridinata x
     * @param y coordinata y
     * @return true se il punto passato è contenuto nella figura, false altrimenti
     */
    public boolean pointIsContained(double x, double y){
        if (((x - this.X) * (x - this.X) + (y - this.Y) * (y - this.Y)) < this.getRadius() * this.getRadius()) {
            return true;
        } else {
            return false;
        }
    }

//(x - a)^2 + (y - b)^2 <= r^2 -> formula punto contenuto in un cerchio a,b coordinate cerchio
}