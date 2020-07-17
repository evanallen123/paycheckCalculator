package com.evaninc.paycheckapplication.dataobject.taxee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IncomeTaxBracket {

    @JsonProperty(value = "bracket")
    private int bracket;
    @JsonProperty(value = "marginal_rate")
    private double marginalRate;

    public int getBracket()
    {
        return bracket;
    }

    public double getMarginalRate()
    {
        return marginalRate;
    }

}


