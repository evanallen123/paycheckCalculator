package com.evaninc.paycheckapplication.dataobject.taxee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaxeeResponse {

    @JsonProperty(value = "single")
    FilingStatus single;
    @JsonProperty(value = "married")
    FilingStatus married;
    @JsonProperty(value = "married_separately")
    FilingStatus marriedSeparately;
    @JsonProperty(value = "head_of_household")
    FilingStatus headOfHousehold;

    public FilingStatus getFilingStatus(String filingStatus) {
        switch(filingStatus)
        {
            case "single":
                return single;
            case "married":
                return married;
            case "married_separately":
                return marriedSeparately;
            case "head_of_household":
                return headOfHousehold;
        }
        return null;
    }

}
