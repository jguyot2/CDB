package com.excilys.model;

import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Classe pour la transmission des valeurs.
 *
 * @author jguyot2
 */
public final class ComputerDTO {
    /**
     * Représentation de l'identifiant de l'entreprise par son identifiant.
     */
    @Nullable
    private CompanyDTO company;

    /**
     * Représentation de la date d'arrêt, au format donné dans DateMapper
     */
    @Nullable
    private String discontinued;

    /**
     * Représentation de la date d'intro, au format donné dans DateMapper
     */
    @Nullable
    private String introduced;

    /**
     * Nom de l'ordinateur.
     */
    @NonNull
    private String name;

    /**
     * Représentation de l'identifiant de l'ordinateur.
     */
    @NonNull
    private Long id = 0L;

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
        this.name = computerName;
        this.id = Long.valueOf(computerId);
        this.company = companyId;
        this.introduced = strIntroductionDate;
        this.discontinued = strDiscontinuationDate;
    }

    public ComputerDTO(final String name, final Long id, final Long companyId,
            final String introduced, final String discontinued) {
        this.name = name;
        this.id = id;
        this.company = new CompanyDTO(companyId);
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    public ComputerDTO(final String name, final Long id, final CompanyDTO company,
            final String introduced, final String discontinued) {
        this.name = name;
        this.id = id;
        this.company = company;
        this.introduced = introduced;
        this.discontinued = discontinued;
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
                && Objects.equals(this.discontinued, other.discontinued)
                && Objects.equals(this.introduced, other.introduced)
                && Objects.equals(this.company, other.company);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof ComputerDTO ? equals((ComputerDTO) o) : false;
    }

    public CompanyDTO getCompany() {
        return this.company;
    }

    public String getDiscontinued() {
        return this.discontinued;
    }

    public Long getId() {
        return this.id;
    }

    public String getIntroduced() {
        return this.introduced;
    }

    public String getName() {
        return this.name;
    }

    public void setCompany(final CompanyDTO newStrEntrepriseId) {
        this.company = newStrEntrepriseId;
    }

    public void setDiscontinued(final String newDiscontinuationDate) {
        this.discontinued = newDiscontinuationDate;
    }

    public void setId(final Long newStrId) {
        this.id = newStrId;
    }

    public void setIntroduced(final String newIntroductionDate) {
        this.introduced = newIntroductionDate;
    }

    public void setName(final String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "(" + this.name + ", " + this.introduced + "-" + this.discontinued + "\t" + this.id
                + "\t" + this.company;
    }
}
