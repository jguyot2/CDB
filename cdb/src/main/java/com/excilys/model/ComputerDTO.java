package com.excilys.model;

import java.util.Objects;

/**
 * Classe pour la transmission des valeurs.
 *
 * @author jguyot2
 */
public final class ComputerDTO {
    /**
     * Représentation de l'identifiant de l'entreprise par son identifiant.
     */
    private CompanyDTO company;

    /**
     * Représentation de la date d'arrêt, au format DD/MM/YYYY.
     */
    private String discontinuationDate;

    /**
     * Représentation de la date d'intro, au format DD/MM/YYY.
     */
    private String introductionDate;

    /**
     * Nom de l'ordinateur.
     */
    private String name;

    /**
     * Représentation de l'identifiant de l'ordinateur.
     */
    private String strId;

    /**
     * @param computerName
     * @param computerId
     * @param companyId
     * @param strIntroductionDate
     * @param strDiscontinuationDate
     */
    public ComputerDTO(final String computerName, final String computerId, final CompanyDTO companyId,
            final String strIntroductionDate, final String strDiscontinuationDate) {
        this.name = computerName;
        this.strId = computerId;
        this.company = companyId;
        this.introductionDate = strIntroductionDate;
        this.discontinuationDate = strDiscontinuationDate;
    }

    /**
     *
     * @param other
     * @return .
     */
    public boolean equals(final ComputerDTO other) {
        if (this == other) {
            return true;
        }
        if (null == other) {
            return false;
        }
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.discontinuationDate, other.discontinuationDate)
                && Objects.equals(this.introductionDate, other.introductionDate)
                && Objects.equals(this.company, other.company);

    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof ComputerDTO ? equals((ComputerDTO) o) : false;
    }

    /**
     * @return la chaîne de caractères correspondant à l'identifiant de l'entreprise
     */
    public CompanyDTO getCompany() {
        return this.company;
    }

    /**
     * @return la date d'arrêt de production
     */
    public String getDiscontinuationDate() {
        return this.discontinuationDate;
    }

    /**
     * @return une chaine représentant l'identifiant de l'ordinateur dans la base
     */
    public String getId() {
        return this.strId;
    }

    /**
     * @return la date d'introduction à la vente
     */
    public String getIntroductionDate() {
        return this.introductionDate;
    }

    /**
     * @return le nom de l'ordinateur
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param newStrEntrepriseId
     */
    public void setCompany(final CompanyDTO newStrEntrepriseId) {
        this.company = newStrEntrepriseId;
    }

    /**
     * @param newDiscontinuationDate
     */
    public void setDiscontinuationDate(final String newDiscontinuationDate) {
        this.discontinuationDate = newDiscontinuationDate;
    }

    /**
     * @param newStrId
     */
    public void setId(final String newStrId) {
        this.strId = newStrId;
    }

    /**
     * @param newIntroductionDate
     */
    public void setIntroductionDate(final String newIntroductionDate) {
        this.introductionDate = newIntroductionDate;
    }

    /**
     * @param newName
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "(" + this.name + ", " + this.introductionDate + "-" + this.discontinuationDate + "\t"
                + this.strId + "\t" + this.company;
    }
}
