package com.excilys.adapters;

/**
 * Énumération permettant de répertorier les problèmes pouvant être associés à une instance
 * ComputerDTO incohérente.
 *
 * @author jguyot2
 *
 */
public enum ComputerDTOProblems {
    NULL_DTO("le dto est égal à null"),
    INVALID_DATE_DISCO("La date correspondant à l'arrêt de production n'est pas bien formée"),
    INVALID_DATE_INTRO(
            "La date correspondant à l'introduction sur le marché n'est pas bien formée"),
    INVALID_ID("L'identifiant est invalide"), INVALID_NAME("Le nom n'est pas valide");

    private String message;

    private ComputerDTOProblems(final String s) {
        this.message = s;
    }

    /**
     * Explication du problème.
     *
     * @return une chaîne expliquant le problème courant d'une instance de ComputerDTO.
     */
    public String getExplanation() {
        return this.message;
    }
}
