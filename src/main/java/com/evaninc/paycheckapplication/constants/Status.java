package com.evaninc.paycheckapplication.constants;

public enum Status {

    SINGLE("single"),
    MARRIED("married"),
    MARRIED_SEPARATELY("married_separately"),
    HEAD_OF_HOUSEHOLD("head_of_household");

    private String name;

    Status(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
