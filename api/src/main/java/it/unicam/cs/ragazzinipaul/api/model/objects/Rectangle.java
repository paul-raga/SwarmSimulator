package it.unicam.cs.ragazzinipaul.api.model.objects;

/**
 * The type Rectangle.
 *
 * @param <K> the type parameter label
 */
public class Rectangle<K> extends Shape implements ImmovableObject{
    private final double X;
    private final double Y;

    private final double width;
    private final double higth;

    private final K label;

    /**
     * Instantiates a new Rectangle.
     *
     * @param x     the x
     * @param y     the y
     * @param width the width
     * @param hight the hight
     * @param label the label
     */
    public Rectangle(double x, double y, double width, double hight, K label) {
        this.X=x;
        this.Y=y;

        this.width=width;
        this.higth=hight;

        this.label= label;
    }

    /**
     *
     * @return l'area del rettangolo
     */
    public double getArea(){
        return this.higth * this.width;
    }

    /**
     *
     * @return la label dell'oggetto
     */
    public K getLabel(){
        return this.label;}

    /**
     *
     * @return la coordinata x del rettangolo
     */
    public double getX(){return this.X;}

    /**
     *
     * @return la coordinata y del rettangolo
     */
    public double getY(){return this.Y;}

    /**
     * Get width double.
     *
     * @return the double
     */
    public double getWidth(){
        return this.width;
    }

    /**
     * Get higth double.
     *
     * @return the double
     */
    public double getHigth(){
        return this.higth;
    }

    /**
     *
     * @param x cooridinata x
     * @param y coordinata y
     * @return true se il punto passato è contenuto nella figura, false altrimenti
     */
    public boolean pointIsContained(double x, double y){
        if( (x < this.width/2 + this.X) && (x > this.X - this.width/2 ) && (y < this.higth/2 + this.Y)  && (y > this.Y - this.higth/2)){
            return true;}
        else{return false;}
    }

}




//X range = x<   width/2 + X, x>   X - width/2
//Y range = y<  higth/2 +Y, y >  Y - hight/2