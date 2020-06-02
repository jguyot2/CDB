package com.excilys.service;

public enum ComputerDTOProblems {
    INVALID_NAME("Le nom n'est pas valide"),
    INEXISTENT_MANUFACTURER("L'id correspondant à l'entreprise n'existe pas"),
    INVALID_MANUFACURER_ENTRY("Le champ correspondant au fabricant est invalide"),
    INEXISTANT_COMPANY_ID("L'id correspondant au fabricant n'existe pas"),
    INVALID_DATE_INTRO(
            "La date correspondant à l'introduction sur le marché n'est pas bien formée"),
    INVALID_DATE_DISCO("La date correspondant à l'arrêt de production n'est pas bien formée"),
    INVALID_ID("L'identifiant est invalide");

    private String message;

    private ComputerDTOProblems(String s) {
        message = s;
    }
    
    public String getMessage() {
        return this.message;
    }
}
