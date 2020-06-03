package com.excilys.model;

import java.util.Objects;

/**
 * Classe représentant une entreprise par son nom + son
 * identifiant sur la base
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
        name = companyName;
    }

    /**
     * Constructeur avec indication de l'identifiant.
     *
     * @param companyName le nom de l'entreprise
     * @param companyId   l'identifiant de l'entreprise
     */
    public Company(final String companyName, final long companyId) {
        name = companyName;
        id = companyId;
    }

    /**
     * Test d'égalité, uniquement à partir des noms des entreprises.
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
        return Objects.equals(name, other.name);
    }

    /**
     * Getter de l'attribut identifiant.
     * @return l'identifiant.
     */
    public long getId() {
        return id;
    }

    /**
     * Getter du nom.
     * @return Le nom de l'entreprise
     */
    public String getName() {
        return name;
    }

    /**
     * Setter de l'identifiant.
     * @param companyId l'identifiant.
     */
    public void setId(final long companyId) {
        id = companyId;
    }

    /** */
    @Override
    public String toString() {
        return name;
    }
}
