package com.excilys.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * Classe représentant un ordinateur.
 *
 * @author jguyot2
 */
@Entity
@Table(name = "computer")
public class Computer implements Serializable {

    public static Builder getBuilder() {
        return new Builder();
    }


    public static class Builder {

        private String computerName;
        private LocalDate computerIntro;
        private LocalDate computerDisco;
        private Company computerManufacturer;
        private Long computerId;


        Builder() {
        }

        public Computer build() {
            return new Computer(this.computerName, this.computerManufacturer, this.computerIntro,
                    this.computerDisco, this.computerId);
        }

        public Builder setDisco(final LocalDate disco) {
            this.computerDisco = disco;
            return this;
        }

        public Builder setId(final Long id) {
            this.computerId = id;
            return this;
        }
        public Builder setIntro(final LocalDate intro) {
            this.computerIntro = intro;
            return this;
        }
        public Builder setManufacturer(final Company manufacturer) {
            this.computerManufacturer = manufacturer;
            return this;
        }

        public Builder setName(final String newName) {
            this.computerName = newName;
            return this;
        }
    }


    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @Column(name = "discontinued")
    @Nullable
    private LocalDate discontinued;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nullable
    @Column(name = "introduced")
    private LocalDate introduced;

    @Nullable
    @JoinColumn(name = "company_id", nullable = true)
    @ManyToOne(targetEntity = Company.class)
    private Company manufacturer;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;


    protected Computer() {
    }

    protected Computer(final Company company) {
        this.manufacturer = company;
    }
    protected Computer(final Computer other) {
        this.discontinued = other.discontinued;
        this.introduced = other.introduced;
        this.name = other.name;
        this.manufacturer = other.manufacturer;
        this.id = other.id;
    }

    protected Computer(final Computer other, final Company c) {
        this(other);
        this.manufacturer = c;
    }

    protected Computer(final String computerName) {
        this.name = computerName;
    }

    protected Computer(final String computerName, final Company computerManufacturer,
            final LocalDate computerIntroduction,
            final LocalDate computerDiscontinuation) {
        this.name = computerName;
        this.manufacturer = computerManufacturer;
        this.introduced = computerIntroduction;
        this.discontinued = computerDiscontinuation;
    }

    protected Computer(final String computerName, final Company computerManufacturer,
            final LocalDate computerIntroduction,
            final LocalDate computerDiscontinuation, final long computerId) {

        this.name = computerName;
        this.manufacturer = computerManufacturer;
        this.introduced = computerIntroduction;
        this.discontinued = computerDiscontinuation;
        this.id = computerId;
    }

    /**
     * Test d'égalité sur les attributs, sauf les identifiants.
     *
     * @param other
     *
     * @return true si les deux instances sont égales, false sinon.
     */
    public boolean equals(final Computer other) {
        if (other == null) {

            return false;
        } else if (this == other) {

            return true;
        }
        return Objects.equals(this.name, other.name) && Objects.equals(this.introduced, other.introduced)
                && Objects.equals(this.discontinued, other.discontinued)
                && Objects.equals(this.manufacturer, other.manufacturer);
    }
    // REFACTO
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Computer other = (Computer) obj;
        if (this.discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!this.discontinued.equals(other.discontinued)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!this.introduced.equals(other.introduced)) {
            return false;
        }
        if (this.manufacturer == null) {
            if (other.manufacturer != null) {
                return false;
            }
        } else if (!this.manufacturer.equals(other.manufacturer)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public LocalDate getDiscontinuation() {
        return this.discontinued;

    }

    public Long getId() {
        return this.id;
    }

    public LocalDate getIntroduction() {
        return this.introduced;
    }

    public Company getManufacturer() {
        return this.manufacturer;
    }

    public String getName() {
        return this.name;
    }

    public String getShortDescription() {
        return "(" + this.id + ", " + this.name + " )";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.discontinued == null ? 0 : this.discontinued.hashCode());
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        result = prime * result + (this.introduced == null ? 0 : this.introduced.hashCode());
        result = prime * result + (this.manufacturer == null ? 0 : this.manufacturer.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    public void setDiscontinuation(final LocalDate newDiscontinuation) {
        this.discontinued = newDiscontinuation;
    }

    public void setId(final long newId) {
        this.id = newId;
    }

    public void setIntroduction(final LocalDate newIntroduction) {
        this.introduced = newIntroduction;
    }

    public void setManufacturer(final Company newManufacturer) {
        this.manufacturer = newManufacturer;
    }

    public void setName(final String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "Computer [" + (this.discontinued != null ? "discontinued=" + this.discontinued + ", " : "")
                + (this.id != null ? "id=" + this.id + ", " : "")
                + (this.introduced != null ? "introduced=" + this.introduced + ", " : "")
                + (this.manufacturer != null ? "manufacturer=" + this.manufacturer + ", " : "")
                + (this.name != null ? "name=" + this.name : "") + "]";
    }
}
