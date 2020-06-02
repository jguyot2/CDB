package com.excilys.model;

public class CompanyDTO {
    /**
     * Identifiant dans la BD.
     */
    private String id;

    /**
     * Nom de l'entreprise.
     */
    private String name;

    /** */
    public CompanyDTO() {
    }

    /**
     * @param companyName
     */
    public CompanyDTO(final String companyName) {
        name = companyName;
    }

    /**
     *
     * @param companyName
     * @param companyId
     */
    public CompanyDTO(final String companyName, final String companyId) {
        name = companyName;
        id = companyId;
    }

    /**
     * @return une chaîne de caractères représentant
     * l'identifiant de l'entreprise
     */
    public String getId() {
        return id;
    }

    /**
     * @return le nom de l'entreprise
     */
    public String getName() {
        return name;
    }

    /**
     * setter pour la représentation de l'identifiant.
     * @param companyId
     */
    public void setId(final String companyId) {
        id = companyId;
    }

    /**
     * Setter pour le nom de l'entreprise.
     * @param companyName
     */
    public void setName(final String companyName) {
        name = companyName;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
