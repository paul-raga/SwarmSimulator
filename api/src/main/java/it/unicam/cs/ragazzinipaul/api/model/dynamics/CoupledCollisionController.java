package it.unicam.cs.ragazzinipaul.api.model.dynamics;

/**
 * classi che implementano questa interfaccia sono responsabili di una feature aggiuntiva, ovvero impartire in comando arbitrario qualora si verifichi una collosione
 * di uno qualsiasi dei robot
 */
public interface CoupledCollisionController {
    void checkAndAct();
}
