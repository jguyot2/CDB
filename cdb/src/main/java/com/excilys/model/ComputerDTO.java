package com.excilys.model;

/**
 * Classe pour la transmission des valeurs.
 *
 * @author jguyot2
 */
public final class ComputerDTO {
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
     * Représentation de l'identifiant de l'entreprise par son
     * identifiant.
     */
    private CompanyDTO strEntrepriseId;

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
    public ComputerDTO(final String computerName, final String computerId,
        final CompanyDTO companyId, final String strIntroductionDate,
        final String strDiscontinuationDate) {
        super();
        name = computerName;
        strId = computerId;
        strEntrepriseId = companyId;
        introductionDate = strIntroductionDate;
        discontinuationDate = strDiscontinuationDate;
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
    public CompanyDTO getStrEntrepriseId() {
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
        discontinuationDate = newDiscontinuationDate;
    }

    /**
     * @param newIntroductionDate
     */
    public void setIntroductionDate(final String newIntroductionDate) {
        introductionDate = newIntroductionDate;
    }

    /**
     * @param newName
     */
    public void setName(final String newName) {
        name = newName;
    }

    /**
     * @param newStrEntrepriseId
     */
    public void setStrEntrepriseId(final CompanyDTO newStrEntrepriseId) {
        strEntrepriseId = newStrEntrepriseId;
    }

    /**
     * @param newStrId
     */
    public void setStrId(final String newStrId) {
        strId = newStrId;
    }
}
