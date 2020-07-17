package com.evaninc.paycheckapplication.dataobject.taxee;

import com.evaninc.paycheckapplication.constants.Status;
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
        Status status = Status.valueOf(filingStatus);
        switch(status)
        {
            case SINGLE:
                return single;
            case MARRIED:
                return married;
            case MARRIED_SEPARATELY:
                return marriedSeparately;
            case HEAD_OF_HOUSEHOLD:
                return headOfHousehold;
        }
        return null;
    }

}
