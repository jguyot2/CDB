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
        name = computerName;
        manufacturer = null;
        introduction = null;
        discontinuation = null;
        id = 0;
    }

    /**
     * @param computerName
     * @param computerManufacturer
     * @param computerIntroduction
     * @param computerDiscontinuation
     */
    public Computer(final String computerName,
        final Company computerManufacturer,
        final LocalDate computerIntroduction,
        final LocalDate computerDiscontinuation) {

        name = computerName;
        manufacturer = computerManufacturer;
        introduction = computerIntroduction;
        discontinuation = computerDiscontinuation;
        id = 0;
    }

    /**
     * @param computerName
     * @param computerManufacturer
     * @param computerIntroduction
     * @param computerDiscontinuation
     * @param computerId
     */
    public Computer(final String computerName,
        final Company computerManufacturer,
        final LocalDate computerIntroduction,
        final LocalDate computerDiscontinuation, final long computerId) {

        name = computerName;
        manufacturer = computerManufacturer;
        introduction = computerIntroduction;
        discontinuation = computerDiscontinuation;
        id = computerId;
    }

    /**
     * Test d'égalité sur les attributs, sauf les identifiants.
     *
     * @param other
     *
     * @return true si les deux instances sont égales, false
     *         sinon.
     */
    public boolean equals(final Computer other) {
        if (other == null) {

            return false;
        } else if (this == other) {

            return true;
        }
        return Objects.equals(name, other.name)
            && Objects.equals(introduction, other.introduction)
            && Objects.equals(discontinuation, other.discontinuation)
            && Objects.equals(manufacturer, other.manufacturer);
    }

    /**
     * @return la date d'interruption de la vente
     */
    public LocalDate getDiscontinuation() {
        return discontinuation;

    }

    /**
     * @return l'identifiant du Computer dans la Bd associée, ou
     *         0 si
     *         l'identifiant n'a pas été défini
     */
    public long getId() {
        return id;
    }

    /**
     * @return la date de début de vente de l'instance.
     */
    public LocalDate getIntroduction() {
        return introduction;
    }

    /**
     * @return le fabricant de l'ordinateur
     */
    public Company getManufacturer() {
        return manufacturer;
    }

    /**
     * @return le nom de l'ordinateur
     */
    public String getName() {
        return name;
    }

    /**
     * Décrit brièvement une instance de Computer.
     *
     * @return une chaîne contenant l'id et le nom de l'instance
     *         courante
     */
    public String getShortDescription() {
        return "(" + id + ", " + name + " )";
    }

    /**
     * @param newDiscontinuation
     */
    public void setDiscontinuation(final LocalDate newDiscontinuation) {
        discontinuation = newDiscontinuation;
    }

    /**
     *
     * @param newId
     */
    public void setId(final long newId) {
        id = newId;
    }

    /**
     * @param newIntroduction
     */
    public void setIntroduction(final LocalDate newIntroduction) {
        introduction = newIntroduction;
    }

    /**
     * @param newManufacturer
     */
    public void setManufacturer(final Company newManufacturer) {
        manufacturer = newManufacturer;
    }

    /**
     * @param newName
     */
    public void setName(final String newName) {
        name = newName;
    }

    /**
     */
    @Override
    public String toString() {
        String representation = "";
        representation += "name=" + name + "\t";
        representation += "manufacturer=" + String.valueOf(manufacturer) + "\t";
        representation += "intro=" + String.valueOf(introduction) + "\t";
        representation += "dicontinuation=" + String.valueOf(discontinuation);
        return representation;
    }
}
