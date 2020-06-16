package com.excilys.model;

public class SortEntry {
    private SortCriteria criteria;
    private boolean ascending;

    public SortEntry(SortCriteria c, boolean isAscendingSort) {
        this.ascending = isAscendingSort;
        this.criteria = c;
    }

    public SortCriteria getCriteria() {
        return this.criteria;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setCriteria(SortCriteria criteria) {
        this.criteria = criteria;
    }

    public static SortEntry fromString(String s) throws IllegalCriteriaStringException {
        String[] values = s.split("-");

        if (values.length == 1) {
            return new SortEntry(SortCriteria.getCriteriaFromString(values[0]), true);
        } else if (values.length == 2) {
            SortCriteria criteria = SortCriteria.getCriteriaFromString(values[0]);
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
}
