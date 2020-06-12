package com.excilys.model;

import java.util.Objects;

/**
 * Classe représentant une entreprise par son nom + son identifiant sur la base
 * de données.
 *
 * @author jguyot2
 */
public class Company {
    /**
     * Identifiant dans la BD de l'entreprise.
     */
    private long id = 0;

    /**
     * Nom de l'entreprise.
     */
    private final String name;

    /**
     * Constructeur d'une entreprise à partir de son nom.
     *
     * @param companyName le nom de l'entreprise
     */
    public Company(final String companyName) {
        this.name = companyName;
    }

    /**
     * Constructeur avec indication de l'identifiant.
     *
     * @param companyName le nom de l'entreprise
     * @param companyId l'identifiant de l'entreprise
     */
    public Company(final String companyName, final long companyId) {
        this.name = companyName;
        this.id = companyId;
    }

    /**
     * Test d'égalité, uniquement à partir des noms des entreprises.
     *
     * @param other
     * @return true si les deux entreprises sont égales, false sinon
     */
    public boolean equals(final Company other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Company other = (Company) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Getter de l'attribut identifiant.
     *
     * @return l'identifiant.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Getter du nom.
     *
     * @return Le nom de l'entreprise
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /**
     * Setter de l'identifiant.
     *
     * @param companyId l'identifiant.
     */
    public void setId(final long companyId) {
        this.id = companyId;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
