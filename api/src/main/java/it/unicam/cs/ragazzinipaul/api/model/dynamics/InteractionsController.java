package it.unicam.cs.ragazzinipaul.api.model.dynamics;

import it.unicam.cs.ragazzinipaul.api.model.objects.Shape;
import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.ragazzinipaul.api.utils.Position;

/**
 * Classi che implementano questa interfaccia vengono associate ad un thread di movimento di un robot, ad ogni variazione di coordinate controllano se il robot ha colliso con una Shape.
 * Inoltre Classi che implementano questa interfaccia possono essere integrate ad uno SwarmController per agire automaticamente in caso di collisioni
 *
 * @param <T> the type parameter estende Robot
 * @param <Q> the type parameter estende Position
 * @param <S> the type parameter estende Shape
 */
public interface InteractionsController<T extends Robot, Q extends Position, S extends Shape>  {
    /**
     * Controlla se il robot ha colliso con una Shape, in caso positivo notifica il Robot di segnalare la condizione
     */
    void check();

}
