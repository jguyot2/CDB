package com.excilys.model;

public enum SortCriterion {
    COMPUTER_NAME("name"), INTRODUCED("introduced"), DISCONTINUED("discontinued"), COMPANY_ID("companyId"),
    COMPANY_NAME("companyName");

    private String repr;

    private SortCriterion(String s) {
        this.repr = s;
    }

    @Override
    public String toString() {
        return this.repr;
    }

    public static SortCriterion getCriteriaFromString(String s) throws IllegalCriteriaStringException {
        if (s == null) {
            throw new IllegalCriteriaStringException();
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
                throw new IllegalCriteriaStringException();
        }
    }
}
