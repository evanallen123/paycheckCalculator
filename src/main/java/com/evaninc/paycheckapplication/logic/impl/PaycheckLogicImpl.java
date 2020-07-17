package com.evaninc.paycheckapplication.logic.impl;

import com.evaninc.paycheckapplication.dataobject.IpApiResponse;
import com.evaninc.paycheckapplication.dataobject.Paycheck;
import com.evaninc.paycheckapplication.dataobject.taxee.TaxeeResponse;
import com.evaninc.paycheckapplication.logic.PaycheckLogic;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Objects;

public class PaycheckLogicImpl implements PaycheckLogic
{
    private final BigDecimal biWeeklyPayPeriodsPerYear = BigDecimal.valueOf(24);
    private final BigDecimal biWeeklyHoursPerPayPeriod = BigDecimal.valueOf(80);


    @Override
    public Paycheck calculateHourlyPaycheck(BigDecimal hourlyPay, BigDecimal hours, HttpServletRequest request)
    {
        String state = getStateByIp(request.getRemoteAddr());
        BigDecimal grossPaycheck = hourlyPay.multiply(hours);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck, state);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount);
        return new Paycheck(
                hourlyPay,
                hours,
                grossPaycheck.multiply(biWeeklyPayPeriodsPerYear),
                grossPaycheck,
                netPaycheck,
                netTaxAmount,
                state
        );
    }

    @Override
    public Paycheck calculateSalaryPaycheck(BigDecimal salaryPay, HttpServletRequest request)
    {
        String state = getStateByIp(request.getRemoteAddr());
        BigDecimal grossPaycheck = salaryPay.divide(biWeeklyPayPeriodsPerYear, RoundingMode.HALF_DOWN);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck, state);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount);
        BigDecimal hourlyPay = grossPaycheck.divide(biWeeklyHoursPerPayPeriod, RoundingMode.HALF_DOWN);
        return new Paycheck(
                hourlyPay,
                biWeeklyHoursPerPayPeriod,
                grossPaycheck.multiply(BigDecimal.valueOf(24)),
                grossPaycheck,
                netPaycheck,
                netTaxAmount,
                state
        );
    }

    private String getStateByIp(String ip)
    {
        IpApiResponse response;
        try
        {
            final String uri = "http://ip-api.com/json/" + ip;
            RestTemplate template = new RestTemplate();
            response = template.getForObject(uri, IpApiResponse.class);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
        if (response != null)
        {
            return response.getState();
        }
        else
        {
            return null;
        }
    }

    private double getStateTax(String state)
    {
        String filingStatus = "single";
        try
        {
            final String uri = "https://taxee.io/api/v2/state/2020/" + state;
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", "Bearer " +
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBU" +
                    "ElfS0VZX01BTkFHRVIiLCJodHRwOi8vdGF4ZWUuaW8vdXNlcl9" +
                    "pZCI6IjVmMGM4ZGFhMzkyZWQyNTNlZjA3MjUzZSIsImh0dHA6L" +
                    "y90YXhlZS5pby9zY29wZXMiOlsiYXBpIl0sImlhdCI6MTU5NDY" +
                    "1ODIxOH0.h5S2jd59HdrvC246hROwQCkzGS7JvBmaI1A3xBhsWyc");
            final HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<TaxeeResponse> response = template.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    TaxeeResponse.class
            );
            if (response.getStatusCode() == HttpStatus.OK)
            {
                return Objects.requireNonNull(response.getBody()).getFilingStatus(filingStatus).getTaxBrackets().get(0).getMarginalRate();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return -1;
    }

    private BigDecimal getTaxedAmount(BigDecimal grossPaycheck, String state)
    {
        final double FEDERAL_TAX_RATE = 12;
        final double STATE_TAX_RATE = getStateTax(state);
        final double SOCIAL_SECURITY_TAX_RATE = 7.65;
        final BigDecimal INCOME_TAX_RATE = BigDecimal.valueOf(FEDERAL_TAX_RATE + STATE_TAX_RATE);
        final BigDecimal HUNDRED = BigDecimal.valueOf(100);

        BigDecimal socialSecurityTax = grossPaycheck.subtract(
                grossPaycheck.divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(SOCIAL_SECURITY_TAX_RATE))
        );
        BigDecimal afterSocialSecurity = grossPaycheck.subtract(socialSecurityTax);
        return (afterSocialSecurity.divide(HUNDRED, RoundingMode.HALF_DOWN).multiply(INCOME_TAX_RATE)).add(socialSecurityTax);

    }
}
