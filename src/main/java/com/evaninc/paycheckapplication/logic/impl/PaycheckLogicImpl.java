package com.evaninc.paycheckapplication.logic.impl;

import com.evaninc.paycheckapplication.dataobject.Paycheck;
import com.evaninc.paycheckapplication.logic.PaycheckLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaycheckLogicImpl implements PaycheckLogic
{
    private final BigDecimal biWeeklyPayPeriodsPerYear = BigDecimal.valueOf(24);
    private final BigDecimal biWeeklyHoursPerPayPeriod = BigDecimal.valueOf(80);


    @Override
    public Paycheck calculateHourlyPaycheck(BigDecimal hourlyPay, BigDecimal hours)
    {

        BigDecimal grossPaycheck = hourlyPay.multiply(hours);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount);
        return new Paycheck(
                hourlyPay,
                hours,
                grossPaycheck.multiply(biWeeklyPayPeriodsPerYear),
                grossPaycheck,
                netPaycheck,
                netTaxAmount
        );
    }

    @Override
    public Paycheck calculateSalaryPaycheck(BigDecimal salaryPay)
    {
        BigDecimal grossPaycheck = salaryPay.divide(biWeeklyPayPeriodsPerYear, RoundingMode.HALF_DOWN);
        BigDecimal netTaxAmount = getTaxedAmount(grossPaycheck);
        BigDecimal netPaycheck = grossPaycheck.subtract(netTaxAmount);
        BigDecimal hourlyPay = grossPaycheck.divide(biWeeklyHoursPerPayPeriod, RoundingMode.HALF_DOWN);
        return new Paycheck(
                hourlyPay,
                biWeeklyHoursPerPayPeriod,
                grossPaycheck.multiply(BigDecimal.valueOf(24)),
                grossPaycheck,
                netPaycheck,
                netTaxAmount
        );
    }

    private BigDecimal getTaxedAmount(BigDecimal grossPaycheck)
    {
        final double FEDERAL_TAX_RATE = 12;
        final double SOCIAL_SECURITY_TAX_RATE = 7.65;
        //final double TAX_RATE = getStateTax() + FEDERAL_TAX_RATE + SOCIAL_SECURITY_TAX_RATE;
        final double TAX_RATE = 20.99139747;

        return grossPaycheck.divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(TAX_RATE));
    }

    private double getStateTax()
    {
        return 4.63;
        //temp comment for deploy test
    }
}
