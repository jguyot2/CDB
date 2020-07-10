package com.excilys.model;

import java.util.Objects;

import org.springframework.lang.Nullable;

public final class CompanyDto {

    @Nullable
    private Long id;

    @Nullable
    private String name;

    public CompanyDto() {
    }

    public CompanyDto(final String companyName) {
        this.name = companyName;
    }

    public CompanyDto(final Long id) {
        this.id = id;
    }

    public CompanyDto(final String companyName, final Long companyId) {
        this.name = companyName;
        this.id = companyId;
    }

    public boolean equals(final CompanyDto other) {
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
        return o instanceof CompanyDto && equals((CompanyDto) o);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(final Long companyId) {
        this.id = companyId;
    }

    public void setName(final String companyName) {
        this.name = companyName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
