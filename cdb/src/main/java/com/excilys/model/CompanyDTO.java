package com.excilys.model;

public class CompanyDTO {
    /**
     * Nom de l'entreprise.
     */
    private String name;

    /**
     * Identifiant dans la BD.
     */
    private String id;

    /** */
    public CompanyDTO() {
    }

    /**
     * @param companyName
     */
    public CompanyDTO(final String companyName) {
        this.name = companyName;
    }

    /**
     * @return une chaîne de caractères représentant
     * l'identifiant de l'entreprise
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return le nom de l'entreprise
     */
    public String getName() {
        return this.name;
    }

    /**
     * setter pour la représentation de l'identifiant.
     * @param companyId
     */
    public void setId(final String companyId) {
        this.id = companyId;
    }

    /**
     * Setter pour le nom de l'entreprise.
     * @param companyName
     */
    public void setName(final String companyName) {
        this.name = companyName;
    }
}
