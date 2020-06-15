package com.excilys.model;

public enum SortCriteria {
    COMPUTER_NAME, INTRODUCED, DISCONTINUED, COMPANY_ID, COMPANY_NAME;

    public static SortCriteria getCriteriaFromString(String s) throws IllegalCriteriaStringException {
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
