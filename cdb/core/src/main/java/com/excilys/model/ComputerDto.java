





package com.excilys.model;

import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * Classe pour la transmission des valeurs.
 *
 * @author jguyot2
 */
public final class ComputerDto {

     /**
      * Représentation de l'identifiant de l'entreprise par son identifiant.
      */
     @Nullable
     private CompanyDto company;

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


     public ComputerDto() {

     }

     public ComputerDto(final String name, final Long id, final Long companyId, final String introduced,
               final String discontinued) {
          this.name = name;
          this.id = id;
          this.company = new CompanyDto(companyId);
          this.introduced = introduced;
          this.discontinued = discontinued;
     }

     // TODO : builder
     public ComputerDto(final String name, final Long id, final CompanyDto company, final String introduced,
               final String discontinued) {
          this.name = name;
          this.id = id;
          this.company = company;
          this.introduced = introduced;
          this.discontinued = discontinued;
     }

     /**
      *
      * @param other
      *
      * @return .
      */
     public boolean equals(final ComputerDto other) {
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
          return o instanceof ComputerDto ? equals((ComputerDto) o) : false;
     }

     public CompanyDto getCompany() {
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

     public void setCompany(final CompanyDto newStrEntrepriseId) {
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
          return "(" + Objects.toString(this.name, "") + ", " + Objects.toString(this.introduced, "") + "-"
                    + Objects.toString(this.introduced, "") + "\t" + Objects.toString(this.id, "") + "\t"
                    + Objects.toString(this.company, "") + ")";
     }
}
