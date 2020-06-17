package com.excilys.model;

public class SortEntry {
    private SortCriterion criteria;
    private boolean ascending;

    public SortEntry(SortCriterion c, boolean isAscendingSort) {
        this.ascending = isAscendingSort;
        this.criteria = c;
    }

    public SortCriterion getCriteria() {
        return this.criteria;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setCriteria(SortCriterion criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return this.criteria.toString() + "-" + (this.ascending ? "asc" : "desc");
    }

    public static SortEntry fromString(String s) throws IllegalCriteriaStringException {
        if (s == null) {
            throw new IllegalCriteriaStringException();
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
                throw new IllegalCriteriaStringException();
            }
            return new SortEntry(criteria, ascending);
        } else {
            throw new IllegalCriteriaStringException();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (this.ascending ? 1231 : 1237);
        result = (prime * result) + ((this.criteria == null) ? 0 : this.criteria.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
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

    public static boolean haveSameCriterion(SortEntry first, SortEntry snd) {
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
}
