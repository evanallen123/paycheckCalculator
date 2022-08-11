package com.evaninc.paycheckapplication.logic.impl;

import com.evaninc.paycheckapplication.dataobject.IpApiResponse;
import com.evaninc.paycheckapplication.dataobject.Paycheck;
import com.evaninc.paycheckapplication.logic.PaycheckLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Slf4j
public class PaycheckLogicImpl implements PaycheckLogic
{
    private final MathContext mc = new MathContext(3);
    private final BigDecimal biWeeklyPayPeriodsPerYear = BigDecimal.valueOf(24);
    private final BigDecimal biWeeklyHoursPerPayPeriod = BigDecimal.valueOf(80);


    @Override
    public Paycheck calculateHourlyPaycheck(BigDecimal hourlyPay, BigDecimal hours, HttpServletRequest request)
    {
        String state = getStateByIp(request.getRemoteAddr());
        log.info("the location of the ip is: {}", state);
        BigDecimal grossPaycheck = hourlyPay.multiply(hours).setScale(2, RoundingMode.UP);
        log.info("Gross Paycheck: {}", grossPaycheck);
        BigDecimal grossSalary = grossPaycheck.multiply(biWeeklyPayPeriodsPerYear);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck, state).setScale(2, RoundingMode.UP);
        log.info("Net tax amount: {}", netTaxAmount);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount).setScale(2, RoundingMode.UP);
        log.info("Net paycheck: {}", netPaycheck);
        log.info("returning new paycheck...");
        return new Paycheck(
                hourlyPay,
                hours,
                grossSalary,
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
        log.info("the location of the ip is: {}", state);
        BigDecimal grossPaycheck = salaryPay.divide(biWeeklyPayPeriodsPerYear, RoundingMode.UP);
        log.info("Gross Paycheck: {}", grossPaycheck);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck, state).setScale(2, RoundingMode.UP);
        log.info("Net tax amount: {}", netTaxAmount);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount).setScale(2, RoundingMode.UP);
        BigDecimal hourlyPay = grossPaycheck.divide(biWeeklyHoursPerPayPeriod, RoundingMode.UP).setScale(2, RoundingMode.UP);
        log.info("Net paycheck: {}, hourlyPay: {}", netPaycheck, hourlyPay);
        log.info("returning new paycheck...");
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

    private double getStateTax(String state, BigDecimal grossSalary)
    {
        return 0.099;
    }

//    private IncomeTaxBracket getTaxBracket(List<IncomeTaxBracket> brackets, BigDecimal grossSalary)
//    {
//        for (IncomeTaxBracket bracket : brackets)
//        {
//        }
//    }

    private BigDecimal getTaxedAmount(BigDecimal grossPaycheck, String state)
    {
        final double FEDERAL_TAX_RATE = 10.54;
        final double STATE_TAX_RATE = getStateTax(state, grossPaycheck);
        final double SOCIAL_SECURITY_TAX_RATE = 7.65;
        final BigDecimal INCOME_TAX_RATE = BigDecimal.valueOf(FEDERAL_TAX_RATE + STATE_TAX_RATE);
        log.info("total tax rate: {}", INCOME_TAX_RATE);
        final BigDecimal HUNDRED = BigDecimal.valueOf(100);

        BigDecimal socialSecurityTax = grossPaycheck
                .divide(BigDecimal.valueOf(100), RoundingMode.UP)
                .multiply(BigDecimal.valueOf(SOCIAL_SECURITY_TAX_RATE));
        log.info("social security tax amount: {}", socialSecurityTax);
        BigDecimal afterSocialSecurity = grossPaycheck
                .subtract(socialSecurityTax);
        log.info("paycheck after social security: {}", afterSocialSecurity);

        BigDecimal totalTaxedAmount = (afterSocialSecurity.divide(HUNDRED, RoundingMode.UP).multiply(INCOME_TAX_RATE)).add(socialSecurityTax);
        log.info("total taxed amount: {}", totalTaxedAmount);
        return totalTaxedAmount.setScale(2, RoundingMode.UP);

    }
}
