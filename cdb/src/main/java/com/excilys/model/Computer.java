package com.excilys.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Classe représentant un ordinateur.
 *
 * @author jguyot2
 */
public class Computer {

    @Nullable
    private LocalDate discontinuation;

    private long id;

    @Nullable
    private LocalDate introduction;

    @Nullable
    private Company manufacturer;

    @NonNull
    private String name;

    public Computer() {
    }

    public Computer(final String computerName) {
        this.name = computerName;
    }

    /**
     * @param computerName
     * @param computerManufacturer
     * @param computerIntroduction
     * @param computerDiscontinuation
     */
    public Computer(final String computerName, final Company computerManufacturer,
            final LocalDate computerIntroduction, final LocalDate computerDiscontinuation) {

        this.name = computerName;
        this.manufacturer = computerManufacturer;
        this.introduction = computerIntroduction;
        this.discontinuation = computerDiscontinuation;

    }

    /**
     * @param computerName
     * @param computerManufacturer
     * @param computerIntroduction
     * @param computerDiscontinuation
     * @param computerId
     */
    public Computer(final String computerName, final Company computerManufacturer,
            final LocalDate computerIntroduction, final LocalDate computerDiscontinuation,
            final long computerId) {

        this.name = computerName;
        this.manufacturer = computerManufacturer;
        this.introduction = computerIntroduction;
        this.discontinuation = computerDiscontinuation;
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
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.introduction, other.introduction)
                && Objects.equals(this.discontinuation, other.discontinuation)
                && Objects.equals(this.manufacturer, other.manufacturer);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Computer) {
            return equals((Computer) o);
        } else {
            return false;
        }
    }

    public LocalDate getDiscontinuation() {
        return this.discontinuation;

    }

    public long getId() {
        return this.id;
    }

    public LocalDate getIntroduction() {
        return this.introduction;
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

    public void setDiscontinuation(final LocalDate newDiscontinuation) {
        this.discontinuation = newDiscontinuation;
    }

    public void setId(final long newId) {
        this.id = newId;
    }

    public void setIntroduction(final LocalDate newIntroduction) {
        this.introduction = newIntroduction;
    }

    public void setManufacturer(final Company newManufacturer) {
        this.manufacturer = newManufacturer;
    }

    public void setName(final String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += "name=" + this.name + "\t";
        representation += "manufacturer=" + String.valueOf(this.manufacturer) + "\t";
        representation += "intro=" + String.valueOf(this.introduction) + "\t";
        representation += "dicontinuation=" + String.valueOf(this.discontinuation);
        return representation;
    }
}
