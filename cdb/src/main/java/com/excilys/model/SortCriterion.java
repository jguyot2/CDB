package com.excilys.model;

public enum SortCriterion {
    COMPUTER_NAME("name"), INTRODUCED("introduced"), DISCONTINUED("discontinued"), COMPANY_ID("companyId"),
    COMPANY_NAME("companyName");

    public static SortCriterion getCriteriaFromString(final String s) throws IllegalCriterionStringException {
        if (s == null) {
            throw new IllegalCriterionStringException();
        }
        switch (s) {
            case "name":
                return COMPUTER_NAME;
            case "introduced":
                return INTRODUCED;
            case "discontinued":
                return DISCONTINUED;
            case "companyId":
                return COMPANY_ID;
            case "companyName":
                return COMPANY_NAME;
            default:
                throw new IllegalCriterionStringException();
        }
    }

    private String repr;

    private SortCriterion(final String s) {
        this.repr = s;
    }

    @Override
    public String toString() {
        return this.repr;
    }
}
