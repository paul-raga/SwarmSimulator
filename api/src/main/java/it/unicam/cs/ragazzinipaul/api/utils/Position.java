package it.unicam.cs.ragazzinipaul.api.utils;

/**
 * Wrapper di coordinate usato come value nelle mappe di robot come key
 */
public class Position implements TwoDimensionsCoordinates {


    private double X;
    private double Y;

    /**
     * Instantiates a new Position.
     *
     * @param x the x
     * @param y the y
     */
    public Position(double x, double y){
        this.X=x;
        this.Y=y;
    }

    /**
     *
     * @return la coordinata x
     */
    @Override
    public double getX() {
        return this.X;
    }

    /**
     *
     * @return la coordinata y
     */
    @Override
    public double getY() {
        return this.Y;
    }


}
