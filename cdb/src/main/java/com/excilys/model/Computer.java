package com.excilys.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe représentant un ordinateur.
 *
 * @author jguyot2
 */
public class Computer {
    /**
     * Date de retrait de la vente sur le marché.
     */
    private LocalDate discontinuation;

    /**
     * Identifiant dans la base de données.
     */
    private long id;
    /**
     * Date d'introduction sur le marché.
     */
    private LocalDate introduction;

    /**
     * Fabricant de l'ordinateur.
     */
    private Company manufacturer;

    /**
     * Nom de l'ordinateur.
     */
    private String name;

    /**
     *
     */
    public Computer() {
    }

    /**
     * Constructeur de l'ordinateur à partir du nom.
     *
     * @param computerName
     */
    public Computer(final String computerName) {
        this.name = computerName;
        this.manufacturer = null;
        this.introduction = null;
        this.discontinuation = null;
        this.id = 0;
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
        this.id = 0;
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
        return Objects.equals(this.name, other.name) && Objects.equals(this.introduction, other.introduction)
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

    /**
     * @return la date d'interruption de la vente
     */
    public LocalDate getDiscontinuation() {
        return this.discontinuation;

    }

    /**
     * @return l'identifiant du Computer dans la Bd associée, ou 0 si l'identifiant
     *         n'a pas été défini
     */
    public long getId() {
        return this.id;
    }

    /**
     * @return la date de début de vente de l'instance.
     */
    public LocalDate getIntroduction() {
        return this.introduction;
    }

    /**
     * @return le fabricant de l'ordinateur
     */
    public Company getManufacturer() {
        return this.manufacturer;
    }

    /**
     * @return le nom de l'ordinateur
     */
    public String getName() {
        return this.name;
    }

    /**
     * Décrit brièvement une instance de Computer.
     *
     * @return une chaîne contenant l'id et le nom de l'instance courante
     */
    public String getShortDescription() {
        return "(" + this.id + ", " + this.name + " )";
    }

    /**
     * @param newDiscontinuation
     */
    public void setDiscontinuation(final LocalDate newDiscontinuation) {
        this.discontinuation = newDiscontinuation;
    }

    /**
     *
     * @param newId
     */
    public void setId(final long newId) {
        this.id = newId;
    }

    /**
     * @param newIntroduction
     */
    public void setIntroduction(final LocalDate newIntroduction) {
        this.introduction = newIntroduction;
    }

    /**
     * @param newManufacturer
     */
    public void setManufacturer(final Company newManufacturer) {
        this.manufacturer = newManufacturer;
    }

    /**
     * @param newName
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     */
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
