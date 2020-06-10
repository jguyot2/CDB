package com.excilys.service;

/**
 * Représente les problèmes possibles sur les instances de Computer
 * @author jguyot2
 *
 */
public enum ComputerInstanceProblems {

    INVALID_DISCONTINUATION_DATE(
            "Date d'arrêt de production invalide car précédant celle de début de production"),

    INVALID_NAME("Nom invalide"),

    NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION(
            "Pas de date de début de production mais une date d'arrêt de production");

    /** */
    private String explanation;

    /**
     * @param errorExplanation explication de l'erreur
     */
    ComputerInstanceProblems(final String errorExplanation) {
        this.explanation = errorExplanation;
    }

    /**
     * @return l'explication associée à l'erreur
     */
    public String getExplanation() {
        return this.explanation;
    }
}
