package com.evaninc.paycheckapplication.dataobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpApiResponse {

    @JsonProperty(value = "country")
    String country;
    @JsonProperty(value = "region")
    String state;

    public String getCountry() {
        return country;
    }

    public String getState()
    {
        return state;
    }

}
