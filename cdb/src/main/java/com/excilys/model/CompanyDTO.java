package com.excilys.model;

import java.util.Objects;

public final class CompanyDTO {
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
        this.name = companyName;
    }

    /**
     *
     * @param companyName
     * @param companyId
     */
    public CompanyDTO(final String companyName, final String companyId) {
        this.name = companyName;
        this.id = companyId;
    }

    public boolean equals(final CompanyDTO other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof CompanyDTO ? equals((CompanyDTO) o) : false;
    }

    /**
     * @return une chaîne de caractères représentant l'identifiant de l'entreprise
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
     * 
     * @param companyId
     */
    public void setId(final String companyId) {
        this.id = companyId;
    }

    /**
     * Setter pour le nom de l'entreprise.
     * 
     * @param companyName
     */
    public void setName(final String companyName) {
        this.name = companyName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
