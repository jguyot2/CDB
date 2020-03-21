package com.excilys.model;

/**
 * Classe pour la transmission des valeurs.
 *
 * @author jguyot2
 */
public class ComputerDTO {
    /**
     * Nom de l'ordinateur.
     */
    private String name;

    /**
     * Représentation de l'identifiant de l'ordinateur.
     */
    private String strId;

    /**
     * Représentation de l'identifiant de l'entreprise par son
     * identifiant.
     */
    private String strEntrepriseId;
    /**
     * Représentation de la date d'intro, au format DD/MM/YYY.
     */
    private String introductionDate;
    /**
     * Représentation de la date d'arrêt, au format DD/MM/YYYY.
     */
    private String discontinuationDate;

    /**
     * @param computerName
     * @param computerId
     * @param companyId
     * @param strIntroductionDate
     * @param strDiscontinuationDate
     */
    public ComputerDTO(final String computerName, final String computerId,
            final String companyId, final String strIntroductionDate,
            final String strDiscontinuationDate) {
        super();
        this.name = computerName;
        this.strId = computerId;
        this.strEntrepriseId = companyId;
        this.introductionDate = strIntroductionDate;
        this.discontinuationDate = strDiscontinuationDate;
    }

    /**
     * @return la date d'arrêt de production
     */
    public String getDiscontinuationDate() {
        return discontinuationDate;
    }

    /**
     * @return la date d'introduction à la vente
     */
    public String getIntroductionDate() {
        return introductionDate;
    }

    /**
     * @return le nom de l'ordinateur
     */
    public String getName() {
        return name;
    }

    /**
     * @return la chaîne de caractères correspondant à
     *         l'identifiant de l'entreprise
     */
    public String getStrEntrepriseId() {
        return strEntrepriseId;
    }

    /**
     * @return une chaine représentant l'identifiant de
     *         l'ordinateur dans la base
     */
    public String getStrId() {
        return strId;
    }

    /**
     * @param newDiscontinuationDate
     */
    public void setDiscontinuationDate(final String newDiscontinuationDate) {
        this.discontinuationDate = newDiscontinuationDate;
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
/**
 * @param newStrEntrepriseId
 */
    public void setStrEntrepriseId(final String newStrEntrepriseId) {
        this.strEntrepriseId = newStrEntrepriseId;
    }

    /**
     * @param newStrId
     */
    public void setStrId(final String newStrId) {
        this.strId = newStrId;
    }
}
