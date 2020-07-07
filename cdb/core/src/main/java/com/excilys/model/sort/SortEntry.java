package com.excilys.model.sort;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class SortEntry {
    /**
     * Création d'un sortEntry à partir d'une chaîne de caractère de la forme
     * suivante : <attribut>-<sens>, où un attribut correspond à une "colonne" (cf
     * SortCriterion), le sens de tri étant "desc" ou "asc" (qui est optionnel)
     *
     * @param s
     * @return
     * @throws IllegalCriterionStringException Si la chaîne en entrée est mal formée
     * @see SortCriterion
     */
    public static SortEntry fromString(@Nullable final String s)
            throws IllegalCriterionStringException {
        if (s == null || s.isEmpty()) {
            throw new IllegalCriterionStringException();
        }
        String[] values = s.split("-");
        if (values.length == 1) {
            return new SortEntry(SortCriterion.getCriteriaFromString(values[0]), true);
        } else if (values.length == 2) {
            SortCriterion criteria = SortCriterion.getCriteriaFromString(values[0]);
            boolean ascending;
            if ("asc".equals(values[1].toLowerCase())) {
                ascending = true;
            } else if ("desc".equals(values[1].toLowerCase())) {
                ascending = false;
            } else {
                throw new IllegalCriterionStringException();
            }
            return new SortEntry(criteria, ascending);
        } else {
            throw new IllegalCriterionStringException();
        }
    }

    /**
     * Détermine si deux SortEntry ont la même colonne de recherche
     *
     * @param first
     * @param snd
     * @return true si les deux critères de tri se font sur le même attribut, false
     *         sinon
     */
    public static boolean haveSameCriterion(@Nullable final SortEntry first,
            @Nullable final SortEntry snd) {
        if (first == null) {
            return snd == null;
        } else {
            return snd != null && first.criteria == snd.criteria;
        }
    }

    private SortCriterion criteria;

    private boolean ascending;

    public SortEntry(@NonNull final SortCriterion c, final boolean isAscendingSort) {
        this.ascending = isAscendingSort;
        this.criteria = c;
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
        SortEntry other = (SortEntry) obj;
        if (this.ascending != other.ascending) {
            return false;
        }
        if (this.criteria != other.criteria) {
            return false;
        }
        return true;
    }

    public SortCriterion getCriteria() {
        return this.criteria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.ascending ? 1231 : 1237);
        result = prime * result + (this.criteria == null ? 0 : this.criteria.hashCode());
        return result;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public void setAscending(final boolean ascending) {
        this.ascending = ascending;
    }

    public void setCriteria(final SortCriterion criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return this.criteria.toString() + "-" + (this.ascending ? "asc" : "desc");
    }
}
