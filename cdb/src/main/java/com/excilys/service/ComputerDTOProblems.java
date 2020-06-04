package com.excilys.service;

public enum ComputerDTOProblems {
    INEXISTANT_COMPANY_ID("L'id correspondant au fabricant n'existe pas"),
    INEXISTENT_MANUFACTURER("L'id correspondant à l'entreprise n'existe pas"),
    INVALID_DATE_DISCO("La date correspondant à l'arrêt de production n'est pas bien formée"),
    INVALID_DATE_INTRO(
        "La date correspondant à l'introduction sur le marché n'est pas bien formée"),
    INVALID_ID("L'identifiant est invalide"),
    INVALID_MANUFACURER_ENTRY("Le champ correspondant au fabricant est invalide"),
    INVALID_NAME("Le nom n'est pas valide");

    private String message;

    private ComputerDTOProblems(final String s) {
        message = s;
    }

    public String getExplanation() {
        return message;
    }
}