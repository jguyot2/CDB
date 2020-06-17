package com.excilys.model;

public class SortEntry {
    public static SortEntry fromString(final String s) throws IllegalCriterionStringException {
        if (s == null) {
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

    public static boolean haveSameCriterion(final SortEntry first, final SortEntry snd) {
        if (first == null) {
            if (snd == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (snd == null) {
                return false;
            } else {
                return first.criteria == snd.criteria;
            }
        }
    }

    private SortCriterion criteria;

    private boolean ascending;

    public SortEntry(final SortCriterion c, final boolean isAscendingSort) {
        this.ascending = isAscendingSort;
        this.criteria = c;
    }

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
