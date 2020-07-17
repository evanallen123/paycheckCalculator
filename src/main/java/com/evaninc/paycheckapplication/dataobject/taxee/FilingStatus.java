package com.evaninc.paycheckapplication.dataobject.taxee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FilingStatus {

    @JsonProperty("income_tax_brackets")
    List<IncomeTaxBracket> taxBrackets;

    public List<IncomeTaxBracket> getTaxBrackets()
    {
        return taxBrackets;
    }
}
