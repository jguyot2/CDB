package com.excilys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Classe représentant une entreprise par son nom + son identifiant sur la base de données.
 *
 * @author jguyot2
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id = 0L;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    public Company() {
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public Company(@NonNull final String companyName) {
        this.name = companyName;
    }

    public Company(@Nullable final String companyName, @Nullable final Long companyId) {
        this.name = companyName;
        this.id = companyId == null ? 0 : companyId;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
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

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    public void setId(final long companyId) {
        this.id = companyId;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
